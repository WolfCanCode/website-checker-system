
<!DOCTYPE html>
<html>
<head>
    <!-- Standard Meta -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <!-- Site Properties -->
    <title>Homepage - Semantic</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">


    <style type="text/css">
        .hidden.menu {
            display: none;
        }
        .masthead.segment {
            min-height: 850px;
            padding: 1em 0em;
            background: url("/assets/wallpaperfinal.jpg") !important;
            background-size: cover !important;
        }
        .masthead .logo.item img {
            margin-right: 1em;
        }
        .masthead .ui.menu .ui.button {
            margin-left: 0.5em;
        }
        .masthead h1.ui.header {
            margin-top: 1.5em;
            margin-bottom: 0em;
            font-size: 4em;
            font-weight: normal;
        }
        .masthead h2 {
            font-size: 1.7em;
            font-weight: normal;
        }
        .ui.vertical.stripe {
            padding: 8em 0em;
        }
        .ui.vertical.stripe h3 {
            font-size: 2em;
        }
        .ui.vertical.stripe .button + h3,
        .ui.vertical.stripe p + h3 {
            margin-top: 3em;
        }
        .ui.vertical.stripe .floated.image {
            clear: both;
        }
        .ui.vertical.stripe p {
            font-size: 1.33em;
        }
        .ui.vertical.stripe .horizontal.divider {
            margin: 3em 0em;
        }
        .quote.stripe.segment {
            padding: 0em;
        }
        .quote.stripe.segment .grid .column {
            padding-top: 5em;
            padding-bottom: 5em;
        }
        .footer.segment {
            padding: 5em 0em;
        }
        .secondary.pointing.menu .toc.item {
            display: none;
        }
        @media only screen and (max-width: 700px) {
            .ui.fixed.menu {
                display: none !important;
            }
            .secondary.pointing.menu .item,
            .secondary.pointing.menu .menu {
                display: none;
            }
            .secondary.pointing.menu .toc.item {
                display: block;
            }
            .masthead.segment {
                min-height: 350px;
            }
            .masthead h1.ui.header {
                font-size: 2em;
                margin-top: 1.5em;
            }
            .masthead h2 {
                margin-top: 0.5em;
                font-size: 1.5em;
            }
        }
    </style>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>

    <script>
        $(document)
            .ready(function() {
                // fix menu when passed
                $('.masthead')
                    .visibility({
                        once: false,
                        onBottomPassed: function() {
                            $('.fixed.menu').transition('fade in');
                        },
                        onBottomPassedReverse: function() {
                            $('.fixed.menu').transition('fade out');
                        }
                    })
                ;
                // create sidebar and attach to menu open
                $('.ui.sidebar')
                    .sidebar('attach events', '.toc.item')
                ;
            })
        ;
    </script>
</head>
<body>

<!-- Following Menu -->
<div class="ui large top fixed hidden menu">
    <div class="ui container ">
        <a class="active item">Home</a>
        <a class="item">Work</a>
        <a class="item">Tests What</a>
        <a class="item">Pricing</a>
        <div class="right menu">
            <div class="item">
                <a class="ui button">Log in</a>
            </div>
            <div class="item">
                <a class="ui primary button">Sign Up</a>
            </div>
        </div>
    </div>
</div>

<!-- Sidebar Menu -->
<div class="ui vertical  sidebar menu">
    <a class="active item">Home</a>
    <a class="item">Tests What</a>
    <a class="item">Pricing</a>
    <a class="item">Login</a>
    <a class="item">Signup</a>
</div>


<!-- Page Contents -->
<div class="pusher">
    <div class="ui vertical masthead center aligned segment">

        <div class="ui container">
            <div class="ui large secondary inverted pointing menu">
                <a class="toc item">
                    <i class="sidebar icon"></i>
                </a>
                <a class="item"> <img src="/assets/icon-wsc.png" style="width:50px;height: auto;margin: auto">
                <font style="margin-left: 15px; font-size: 20px; color: white">Website Checker System</font></a>
                <a class="active item right">Home</a>
                <a class="item">Tests What</a>
                <a class="item">Pricing</a>
                <div class="item">
                    <a class="ui inverted button">Log in</a>
                    <a class="ui inverted button">Sign Up</a>
                </div>
            </div>
        </div>

        <div class="ui text container">
            <h1 class="ui inverted header">
                Check your website first
            </h1>
            <h2 class="ui inverted header">Please give it to us, we make it better. Improve your SEO, spelling checker, your security, any risks...</h2>
            <div class="ui big icon labeled input">
                <div class="ui label" style="color:green">
                    <a onclick="if(this.innerText==='http://'){this.innerText='https://'} else{this.innerText='http://'}">http://</a>
                </div>
                <input type="text" placeholder="mysite.com">
                <i class="search icon"></i>
            </div>
        </div>

    </div>





    <div class="ui inverted vertical footer segment">
        <div class="ui container">
            <div class="ui stackable inverted divided equal height stackable grid">
                <div class="three wide column">
                    <h4 class="ui inverted header">About</h4>
                    <div class="ui inverted link list">
                        <a href="#" class="item">Sitemap</a>
                        <a href="#" class="item">Contact Us</a>
                    </div>
                </div>
                <div class="three wide column">
                    <h4 class="ui inverted header">Services</h4>
                    <div class="ui inverted link list">
                        <a href="#" class="item">Pricing</a>
                        <a href="#" class="item">DNA FAQ</a>
                        <a href="#" class="item">How To Test</a>
                    </div>
                </div>
                <div class="seven wide column">
                    <h4 class="ui inverted header">Website Checker System</h4>
                    <p>Check your site, make it better, grow your bussiness</p>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>
