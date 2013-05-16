## Target
This plugin aims to improve work with EMC WDK components. Current version developed for WDK 6.5.
## What plugin can do

It provides improvements such as:

1. Navigation from configuration to another elements - quick navigation from class to its
usages in configuration files, jsp navigation, 'extends=...' and 'modifies=...' navigation,<br>
![Navigate to component][navigateToComponent.png]
1. 'Find usages' of NLS bundles, components, actions,<br>
![Find usages][FindNlsUsages.png]
1. NLS overrides/overridden properties,<br>
![Overrides/Overridden][nlsGutter.png]
1. Inspections(TODO will be described later),<br>
![Unresolved NLS][unresolvedNlsInProp.png]
1. Autocompletion,
1. Quickfixes and intention actions.

## Future plans
All planned futures and improvements can be viewed [here](http://crazyproger.myjetbrains.com/youtrack/search/?q=project%3A+WT+%23Unresolved+%23Feature).

## Installation
Plugin is not yet published in JetBrains plugin repository, but last(and not only) release can be obtained [here](https://tom-crazyproger.rhcloud.com/nexus/service/local/repositories/releases/content/ru/crazyproger/plugins/webtoper/0.5.1/webtoper-0.5.1.jar)(this is cloud storage - if you've got 503 error then wait 1-2 minutes and try again).
Current version supports IDEA Ultimate Edition since 12.0.4 version.
To install:

1. Download plugin
2. Open IDEA and navigate to Settings(Ctrl+Alt+S)->Plugins and push "Install plugin from disk..."
![Install button][installButton.png]
3. Choose dowloaded file and restart IDEA

## Configuration
By default plugin is disabled for all projects, to enable it your project must contain properly configured [Web facets](http://www.jetbrains.com/idea/webhelp/enabling-web-application-support.html).
Plugin is configured by creating facets of type 'Webtoper' and attaching them to [Web facets](http://www.jetbrains.com/idea/webhelp/enabling-web-application-support.html).

**Note**: you should create 'Webtoper' facet for each 'Web facet' included in your target WDK application.

To attach Webtoper to one Web facet you should:

1. Go to 'Project Structure' and choose 'Facets' item in 'Project settings' at left side:<br>
![Project Structure][pStructure.png]
2. Click 'Add'(Alt+Insert) button and choose 'Webtoper' facet type<br>
![Choose 'Webtoper' facet][pStructureWebtoperType.png]
3. Then select Web facet to which you want attach it<br>
![Select parent facet][selectParentFacet.png]
4. Choose NLS root folder for current facet. NLS root - is the folder with nls files with full directory hierarchy, like source root for .java files<br>
![Choose NLS root][selectNlsRoot.png]

After this IDEA will reparse some files and you can use all provided features of plugin.

## Contribution

If you found a bug in plugin you can submit an issue [here](http://crazyproger.myjetbrains.com/youtrack/dashboard#newissue=yes).<br>
If before submitting you will try to find this error within existing issues - then God will love you!<br>
If you want to contribute to project, then pull requests - is what you need.

Issue tracker is [here](http://crazyproger.myjetbrains.com/youtrack/dashboard).

[installButton.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/installButton.png "Install plugin from disk..."
[unresolvedNlsInProp.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/inspections/unresolvedNlsInProp.png "Unresolved NLS"
[findNlsUsages.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/findNlsUsages.png "Find usages"
[navigateToComponent.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/navigateToComponent.png "Navigate to modified component"
[nlsGutter.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/nlsGutter.png "Overrides/Overridden properties"
[pStructure.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/configuration/pStructure.png "Project Structure"
[pStructureWebtoperType.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/configuration/pStructureWebtoperType.png "Choose 'Webtoper' type"
[selectParentFacet.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/configuration/selectParentFacet.png "Select parent facet"
[selectNlsRoot.png]: https://raw.github.com/wiki/crazyproger/webtoper/img/configuration/selectNlsRoot.png "Select NLS root"