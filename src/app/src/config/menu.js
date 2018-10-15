

const menu = [
    {
        name: "Dashboard",
        to: "/admin",
        key: "d",
        items : null
    },
    {
        name: "SiteMap",
        to: "/admin/sitemap",
        key: "sitemap",
        alt: "The sitemap of page",
        items : null
    },

    {
        name: "Quality",
        items: [
            {
                name: "Spelling test",
                to: "/admin/spelling",
                key: "test1",
                alt: "Spelling test for checking right or wrong spelling in your website"
            },
            {
                name: "Grammar test",
                to: "/admin/grammar",
                key: "test 2",
                alt: "This for test alt title"
            },{
                name: "Broken links test",
                to: "/admin/brokenLinks",
                key: "test3",
                alt: "This for test alt title"
            },{
                name: "Broken pages test",
                to: "/admin/brokenPages",
                key: "test4",
                alt: "This for test alt title"
            },{
                name: "Missing files test",
                to: "/admin/missingFiles",
                key: "test5",
                alt: "This for test alt title"
            },{
                name: "Prohibited content test",
                to: "/admin/prohibited",
                key: "test6",
                alt: "This for test alt title"
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
                alt: "This for test alt title"
            },
            {
                name: "Speed test",
                to: "/admin/speedTest",
                key: "test8",
                alt: "This for test alt title"
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
                alt: "This for test alt title"
            },
            {
                name: "Law Cookie Test",
                to: "/admin/cookielaw",
                key: "test11",
                alt: "This for test alt title"
            },{
                name: "Server Behavior Test",
                to: "/admin/serverBehavior",
                key: "test12",
                alt: "This for test alt title"
            },
        ]
    },{
        name: "Content Test",
        items: [
            {
                name: "Contact Test",
                to: "/admin/contact",
                key: "test13",
                alt: "This for test alt title"
            },
            {
                name: "Direction Test",
                to: "/admin/direction",
                key: "test14",
                alt: "This for test alt title"
            },{
                name: "Pages Test",
                to: "/admin/pages",
                key: "test15",
                alt: "This for test alt title"
            }
        ]
    }


];

export default menu;