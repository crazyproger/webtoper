package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

/**
 * @author crazyproger
 */
public abstract class AbstractNlsReference<T extends PsiElement> extends PsiReferenceBase<T> {

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
}
