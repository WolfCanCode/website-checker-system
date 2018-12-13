import BrokenLinks from '../assets/BrokenLinks.png';
import BrokenPage from '../assets/brokenPage.png';
import Spelling from '../assets/spelling.png';
import Grammar from '../assets/grammar.png';
import SpeedTest from '../assets/desktopSpeed.png';
import MissingFile from '../assets/missingFile.png';
import Favicons from '../assets/favicon.png';
import Prohibited from '../assets/prohibited.PNG';

export const menu = [
    {
        name: "Dashboard",
        to: "/admin/home",
        key: "d",
        items: null
    },
    {
        name: "SiteMap",
        to: "/admin/sitemap",
        key: "sitemap",
        alt: "The sitemap of page",
        items: null
    },
    {
        name: "Quality",
        items: [
            {
                name: "Spelling test",
                to: "/admin/spelling",
                key: "test1",
                alt: "Spelling test for checking right or wrong spelling in your website",
                image: Spelling
            },
            {
                name: "Grammar test",
                to: "/admin/grammar",
                key: "test 2",
                alt: "This for test alt title",
                image: Grammar
            }, {
                name: "Broken links test",
                to: "/admin/brokenLinks",
                key: "test3",
                alt: "Broken links test for checking internal and external links in your website",
                image: BrokenLinks
            }, {
                name: "Broken pages test",
                to: "/admin/brokenPages",
                key: "test4",
                alt: "Broken pages test for checking all pages in your website",
                image: BrokenPage
            }, {
                name: "Missing files test",
                to: "/admin/missingFiles",
                key: "test5",
                alt: "This for test alt title",
                image: MissingFile
            }, {
                name: "Prohibited content test",
                to: "/admin/prohibited",
                key: "test6",
                alt: "Prohibited content test for checking all prohibited word in your website",
                image: Prohibited
            },

        ]
    },
    {
        name: "Experience",
        items: [
            {
                name: "Mobile layout test",
                to: "/admin/mobileLayout",
                key: "test7",
                alt: "Mobile layout test for checking all page layout in mobile screen"
            },
            {
                name: "Speed test",
                to: "/admin/speedTest",
                key: "test8",
                alt: "This for test alt title",
                image: SpeedTest
            },

        ]
    },

    {
        name: "Technology Test",
        items: [
            {
                name: "Javascript Test",
                to: "/admin/javascripterror",
                key: "test9",
                alt: "This for test alt title"
            },
            {
                name: "Favicons Test",
                to: "/admin/favicons",
                key: "test10",
                alt: "This for test alt title",
                image: Favicons
            },
            {
                name: "Law Cookie Test",
                to: "/admin/cookielaw",
                key: "test11",
                alt: "Law Cookie Test for checking all cookies are used in your website"
            }, {
                name: "Server Behavior Test",
                to: "/admin/serverBehavior",
                key: "test12",
                alt: "This for test alt title"
            },
        ]
    }, {
        name: "Content Test",
        items: [
            {
                name: "Contact Details",
                to: "/admin/contact",
                key: "test13",
                alt: "This for test alt title"
            },
            {
                name: "Redirection Test",
                to: "/admin/direction",
                key: "test14",
                alt: "This for test alt title"
            }, {
                name: "Pages Test",
                to: "/admin/pages",
                key: "test15",
                alt: "This for test alt title"
            }
        ]
    }


];

export const menuMan = [
    {
        name: "Dashboard",
        to: "/manager/home",
        key: "d",
        items: null
    },
    {
        name: "Manage Staff",
        to: "/manager/managestaff",
        key: "managestaff",
        items: null
    },
    {
        name: "Manage Website",
        to: "/manager/managewebsite",
        key: "managewebsite",
        items: null
    },
    {
        name: "Manage Wordlist",
        to: "/manager/managewordlist",
        key: "manageworldlist",
        items: null
    },
    // {
    //     name: "Setting",
    //     to: "/manager/setting",
    //     key: "config",
    //     alt: "Configuration and update website",
    //     items: null,
    // },
]

export const menuGuest = [
    {
        name: "Dashboard",
        to: "/guest/home",
        key: "d",
        items: null
    },
    {
        name: "SiteMap",
        to: "/#",
        key: "sitemap",
        alt: "The sitemap of page",
        items: null
    },
    {
        name: "Quality",
        items: [
            {
                name: "Spelling test",
                to: "/#",
                key: "test1",
                alt: "Spelling test for checking right or wrong spelling in your website",
                image: Spelling
            },
            {
                name: "Grammar test",
                to: "/#",
                key: "test 2",
                alt: "This for test alt title",
                image: Grammar
            }, {
                name: "Broken links test",
                to: "/#",
                key: "test3",
                alt: "Broken links test for checking internal and external links in your website",
                image: BrokenLinks
            }, {
                name: "Broken pages test",
                to: "/#",
                key: "test4",
                alt: "Broken pages test for checking all pages in your website",
                image: BrokenPage
            }, {
                name: "Missing files test",
                to: "/#",
                key: "test5",
                alt: "This for test alt title",
                image: MissingFile
            }, {
                name: "Prohibited content test",
                to: "/#",
                key: "test6",
                alt: "Prohibited content test for checking all prohibited word in your website",
                image: Prohibited
            },

        ]
    },
    {
        name: "Experience",
        items: [
            {
                name: "Mobile layout test",
                to: "/#",
                key: "test7",
                alt: "Mobile layout test for checking all page layout in mobile screen"
            },
            {
                name: "Speed test",
                to: "/#",
                key: "test8",
                alt: "This for test alt title",
                image: SpeedTest
            },

        ]
    },

    {
        name: "Technology Test",
        items: [
            {
                name: "Javascript Test",
                to: "/#",
                key: "test9",
                alt: "This for test alt title"
            },
            {
                name: "Favicons Test",
                to: "/admin/favicons",
                key: "test10",
                alt: "This for test alt title",
                image: Favicons
            },
            {
                name: "Law Cookie Test",
                to: "/#",
                key: "test11",
                alt: "Law Cookie Test for checking all cookies are used in your website"
            }, {
                name: "Server Behavior Test",
                to: "/admin/serverBehavior",
                key: "test12",
                alt: "This for test alt title"
            },
        ]
    }, {
        name: "Content Test",
        items: [
            {
                name: "Contact Details",
                to: "/#",
                key: "test13",
                alt: "This for test alt title"
            },
            {
                name: "Redirection Test",
                to: "/#",
                key: "test14",
                alt: "This for test alt title"
            }, {
                name: "Pages Test",
                to: "/#",
                key: "test15",
                alt: "This for test alt title"
            }
        ]
    }


];

export default [menu, menuMan, menuGuest];