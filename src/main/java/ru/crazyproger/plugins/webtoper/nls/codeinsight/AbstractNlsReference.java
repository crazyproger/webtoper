package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.LocalQuickFixProvider;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

/**
 * @author crazyproger
 */
public abstract class AbstractNlsReference<T extends PsiElement> extends PsiReferenceBase<T> implements LocalQuickFixProvider {

    private static final Logger LOG = Logger.getInstance("#" + AbstractNlsReference.class.getName());

    @Nullable
    protected final PsiElement resolveTo;

    public AbstractNlsReference(T element, @Nullable PsiElement resolveTo) {
        super(element, ElementManipulators.getValueTextRange(element));
        this.resolveTo = resolveTo;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return resolveTo;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return EMPTY_ARRAY;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {

        PsiFile file = element.getContainingFile();
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) throw new IllegalArgumentException("Can't bind to element " + element);
        String name = NlsUtils.getNlsName(virtualFile, element.getProject());
        if (name == null) throw new IllegalArgumentException("Can't bind to element " + element);
        return getManipulator().handleContentChange(myElement, getRangeInElement(), name);
    }

    protected ElementManipulator<T> getManipulator() {
        ElementManipulator<T> manipulator = ElementManipulators.getManipulator(myElement);
        if (manipulator == null) {
            LOG.error("Cannot find manipulator for " + myElement + " in " + this + " class " + getClass());
        }
        return manipulator;
    }

    protected abstract String getElementText();

    @Nullable
    @Override
    public LocalQuickFix[] getQuickFixes() {
        return new LocalQuickFix[]{new LocalQuickFix() {
            @NotNull
            @Override
            public String getName() {
                return WebtoperBundle.message("create.nls.file.quickfix.text", getElementText());
            }

            @NotNull
            @Override
            public String getFamilyName() {
                return WebtoperBundle.message("create.nls.file.quickfix.familyName");
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                String nameText = getElementText();
                Module module = ModuleUtil.findModuleForPsiElement(getElement());
                VirtualFile[] nlsRoots = NlsUtils.getNlsRoots(module);
                assert nlsRoots.length > 0 : "Nls files without nls roots should not exists";

                VirtualFile nlsRoot = nlsRoots[0]; // todo here should be dialog with asking what root to use, like source root choose


            }
        }};
    }
}
