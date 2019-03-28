package webtestimator.model.enums;
/**
 * Enum containing all the supported CMS. Can easily be extended or modified.
 * */
public enum CMS {
    WORDPRESS("WordPress"),
    JOOMLA("Joomla"),
    DRUPAL("Drupal"),
    VBULLETIN("vBulletin"),
    BLOGGER("Blogger"),
    TYPO3("Typo3"),
    UMBRACO("Umbraco"),
    SITECORE("Sitecore"),
    MAGNOLIA("Magnolia"),
    TYPESETTER("Typesetter"),
    MEDIAWIKI("MediaWiki"),
    OSCOMMENCE("osCommence"),
    BITRIX("Bitrix"),
    SHAREPOINT("Sharepoint"),
    SPIP("Spip"),
    EKTRON("Ektron"),
    XOOPS("Xoops"),
    SITEFINITY("Sitefinity"),
    PLONE("Plone"),
    LIFERAY("Liferay"),
    TYPEPAD("TypePad"),
    SQUARESPACE("Squarespace"),
    CONTAO("Contao"),
    PHPNUKE("PHP-Nuke"),
    EPISERVER("EPIServer"),
    VIVVO("Vivvo"),
    SILVERSTRIPE("Silverstripe"),
    CONCRETE5("concrete5"),
    XPRESSENGINE("XpressEngine"),
    HOMESTEAD("Homestead"),
    PHPFUSION("PHP-Fusion"),
    MAMBO("Mambo"),
    ALTERIAN("Alterian"),
    SERENDIPITY("Serendipity"),
    B2EVOLUTION("b2evolution"),
    COMMONSPOT("CommonSpot"),
    KENTICO("Kentico"),
    IMPERIA("Imperia"),
    CONVIO("Convio");


    private String name;

    private CMS(String name) {
        this.name = name;
    }

}
