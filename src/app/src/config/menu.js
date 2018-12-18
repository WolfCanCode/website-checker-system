import BrokenLinks from '../assets/BrokenLinks.png';
import BrokenPage from '../assets/brokenPage.png';
import Spelling from '../assets/spelling.png';
import Grammar from '../assets/grammar.png';
import SpeedTest from '../assets/desktopSpeed.png';
import MissingFile from '../assets/missingFile.png';
import Favicons from '../assets/favicon.png';
import Prohibited from '../assets/prohibited.png';

export const menu = [
    // {
    //     name: "Dashboard",
    //     to: "/admin/home",
    //     key: "d",
    //     items: null
    // },
    {
        name: "SiteMap",
        to: "/admin/home",
        key: "d",
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
                alt: "Grammar test for checking right or woring phrase, tense and some clau",
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
                alt: "Missing file test for checking file file used by a webpage (such as an image, document or stylesheet) that was not loaded for any reason.",
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
                alt: "Speed Test for checking the performance of website about how fast the website be done or be usable",
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
                alt: "Javascript test for checking if the website have any javascript error"
            },
            {
                name: "Favicons Test",
                to: "/admin/favicons",
                key: "test10",
                alt: "Favicon test for checking if the website has correct favicon or not",
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
                alt: "Server Behavior Test for checking if the website server is going right or not."
            },
        ]
    }, {
        name: "Content Test",
        items: [
            {
                name: "Contact Details",
                to: "/admin/contact",
                key: "test13",
                alt: "This test lists all contact details – phone numbers and email addresses – that it is able to detect within the website."
            },
            {
                name: "Redirection Test",
                to: "/admin/direction",
                key: "test14",
                alt: "This test identifies redirections, i.e. where visiting a URL redirects to another URL."
            }, {
                name: "Pages Test",
                to: "/admin/pages",
                key: "test15",
                alt: "This test identifies title, canonical url and status code of webpage."
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
        name: "Quality",
        items: [
            {
                name: "Spelling test (lock)",
                to: "/guest/spelling",
                key: "test1",
                alt: "Spelling test for checking right or wrong spelling in your website",
                image: Spelling
            },
            {
                name: "Grammar test (lock)",
                to: "/guest/grammar",
                key: "test 2",
                alt: "This for test alt title",
                image: Grammar
            }, {
                name: "Broken links test",
                to: "/guest/brokenLink",
                key: "test3",
                alt: "Broken links test for checking internal and external links in your website",
                image: BrokenLinks
            }, {
                name: "Broken pages test (lock)",
                to: "/guest/brokenPages",
                key: "test4",
                alt: "Broken pages test for checking all pages in your website",
                image: BrokenPage
            }, {
                name: "Missing files test",
                to: "/guest/missingFile",
                key: "test5",
                alt: "Missing file test for checking file file used by a webpage (such as an image, document or stylesheet) that was not loaded for any reason.",
                image: MissingFile
            }, {
                name: "Prohibited content test (lock)",
                to: "/guest/prohibited",
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
                name: "Mobile layout test (lock)",
                to: "/guest/mobileLayout",
                key: "test7",
                alt: "Mobile layout test for checking all page layout in mobile screen"
            },
            {
                name: "Speed test (lock)",
                to: "/guest/speedTest",
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
                name: "Javascript Test (lock)",
                to: "/guest/javascripterror",
                key: "test9",
                alt: "This for test alt title"
            },
            {
                name: "Favicons Test (lock)",
                to: "/guest/favicons",
                key: "test10",
                alt: "This test checks whether this website correctly defines Favicons, which are icons used to represent the website.",
                image: Favicons
            },
            {
                name: "Law Cookie Test (lock)",
                to: "/guest/cookielaw",
                key: "test11",
                alt: "Law Cookie Test for checking all cookies are used in your website"
            }, {
                name: "Server Behavior Test (lock)",
                to: "/guest/serverBehavior",
                key: "test12",
                alt: "This for test alt title"
            },
        ]
    }, {
        name: "Content Test",
        items: [
            {
                name: "Contact Details (lock)",
                to: "/guest/contact",
                key: "test13",
                alt: "This test lists all contact details – phone numbers and email addresses – that it is able to detect within the website."
            },
            {
                name: "Redirection Test (lock)",
                to: "/guest/direction",
                key: "test14",
                alt: "This test identifies redirections, i.e. where visiting a URL redirects to another URL."
            }, {
                name: "Pages Test (lock)",
                to: "/guest/pages",
                key: "test15",
                alt: "This test identifies title, canonical url and status code of webpage."
            }
        ]
    }


];

export default [menu, menuMan, menuGuest];