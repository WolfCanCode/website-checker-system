import React, { Component } from 'react';
import { Segment } from 'semantic-ui-react';

export default class Canvas extends Component {

    componentDidMount() {

        var canvas = document.getElementById("myCanvas");
        var context = canvas.getContext("2d");

        context.beginPath();

        /* properties */
        context.font = '12px serif'

        /* end properties */

        var rectCoord = []
        // eslint-disable-next-line 
        const map = new Array([1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [0, 0, 0, 0, 0, 0, 0, 8, 8, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 25, 25, 25, 25, 25, 0, 0, 0, 0, 0, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 32, 32, 32, 32, 32, 32, 32], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 6, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
        // eslint-disable-next-line 
        const typeMap = new Array(
            [1], [1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1], [0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
        // eslint-disable-next-line 
        const urlMap = new Array(
            ['https://block.vn'], ['https://block.vn/', '#', 'https://block.vn/dieu-khoan-su-dung', 'https://block.vn/chinh-sach-bao-mat', 'https://block.vn/giai-quyet-tranh-chap', 'https://block.vn/cau-hoi-thuong-gap', 'https://block.vn/chinh-sach-hoan-hoc-phi', 'https://block.vn/tao-moi-khoa-hoc', 'https://block.vn/dang-nhap', 'mailto:hotro@block.vn', 'https://block.vn/gioi-thieu', 'https://block.vn/dang-nhap#dang-ky', 'https://block.vn/khoa-hoc/ban-hang-gia-toc-voi-san-pham-so-va-affiliate', 'https://block.vn/khoa-hoc/video-marketing-chu-viet-tay-tay-viet-bang', 'https://block.vn/khoa-hoc/kiem-tien-dollar-voi-ebay-droshipping', 'https://block.vn/khoa-hoc/setup-he-thong-kinh-doanh-online-tu-dau-den-tu-dong-hoan-toan', 'https://block.vn/khoa-hoc/dung-phim-truyen-thong-inh-cao-voi-adobe-premiere', 'https://block.vn/khoa-hoc/khoa-hoc-cinema-4d-basic', 'https://block.vn/khoa-hoc/power-point-thuyet-trinh-motion-graphic-power-point-video-marketing', 'https://block.vn/khoa-hoc/-do-hoa-thiet-ke-dinh-cao-voi-illustrator', 'https://block.vn/khoa-hoc/photoshop-marketing-dinh-cao', 'https://block.vn/khoa-hoc/video-marketing-3-gio-pro', 'https://block.vn/khoa-hoc/lam-video-tin-tuc-nuoc-ngoai-kiem-tien-youtube-usa', 'https://block.vn/khoa-hoc/text-animation-dinh-cao', 'https://block.vn/tim-kiem', 'https://www.facebook.com/blockvn/', 'https://www.youtube.com/channel/UCh61g1QSq2qDiea4aHlk_MQ', 'https://www.instagram.com/block.vn/', 'http://online.gov.vn/HomePage/WebsiteDisplay.aspx?DocId=26062', 'https://block.vn/phuc-hoi-mat-khau', 'https://block.vn/facebookAuthenticationDialog', 'https://block.vn/googleAuthenticationDialog'], ['', '', '', '', '', '', '', 'http://info.block.vn/course-quality-review/', 'http://info.block.vn/course-creation/', '', '', 'course-filter.html', '', '', '', '', '', '', '', '', '', '', '', '', '', 'https://block.vn/khoa-hoc/react-co-ban', 'https://block.vn/khoa-hoc/java-co-ban', 'https://block.vn/khoa-hoc/java-exception-logging-api', 'https://block.vn/khoa-hoc/java-generics-collections', 'https://block.vn/khoa-hoc/java-regular-expression', 'https://block.vn/khoa-hoc/java-nang-cao', 'https://block.vn/khoa-hoc/xay-dung-ung-dung-java-web-chuyen-nghiep-voi-servlet-jsp', '', '', '', '', '', 'https://block.vn/reg/?cid=102&locale2=vi_VN&refid=9', 'https://block.vn/reg/?cid=103&locale2=vi_VN&refid=9', 'https://lm.facebook.com/l.php?u=https%3A%2F%2Fblock.vn%2Floginwithfacebook%3Ferror%3Daccess_denied%26error_code%3D200%26error_description%3DPermissions%2Berror%26error_reason%3Duser_denied%23_%3D_&h=AT0pR6_351V4-f2Y4vbNGtyN02dawNTbEWi4ikLAhw3xAwyC9oFw7usKiIs3OaYW2PEJEb2GRfbJMuXon9L9kMB4RQsniGHeDaYuXseecLZHW64htIl3W8rV0I_ByJIdp7ZjPkfeRv5rxSKO', 'https://block.vn/recover/initiate/?c=https%3A%2F%2Fm.facebook.com%2Flogin.php%3Fskip_api_login%3D1%26api_key%3D176087696194193%26signed_next%3D1%26next%3Dhttps%253A%252F%252Fm.facebook.com%252Fv2.8%252Fdialog%252Foauth%253Fredirect_uri%253Dhttps%25253A%25252F%25252Fblock.vn%25252Floginwithfacebook%2526scope%253Demail%25252Cpublic_profile%2526client_id%253D176087696194193%2526ret%253Dlogin%2526logger_id%253Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1%26cancel_url%3Dhttps%253A%252F%252Fblock.vn%252Floginwithfacebook%253Ferror%253Daccess_denied%2526error_code%253D200%2526error_description%253DPermissions%252Berror%2526error_reason%253Duser_denied%2523_%253D_%26display%3Dtouch%26locale%3Dvi_VN%26logger_id%3Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1&ars=facebook_login&lwv=100&locale2=vi_VN&refid=9', 'https://block.vn/help/?locale2=vi_VN&refid=9', 'https://block.vn/a/language.php?l=zh_TW&lref=https%3A%2F%2Fm.facebook.com%2Flogin.php%3Fskip_api_login%3D1%26api_key%3D176087696194193%26signed_next%3D1%26next%3Dhttps%253A%252F%252Fm.facebook.com%252Fv2.8%252Fdialog%252Foauth%253Fredirect_uri%253Dhttps%25253A%25252F%25252Fblock.vn%25252Floginwithfacebook%2526scope%253Demail%25252Cpublic_profile%2526client_id%253D176087696194193%2526ret%253Dlogin%2526logger_id%253Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1%26cancel_url%3Dhttps%253A%252F%252Fblock.vn%252Floginwithfacebook%253Ferror%253Daccess_denied%2526error_code%253D200%2526error_description%253DPermissions%252Berror%2526error_reason%253Duser_denied%2523_%253D_%26display%3Dtouch%26locale%3Dvi_VN%26logger_id%3Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1&index=2&gfid=AQBFidUVLmCbf1ee&ref_component=mbasic_footer&ref_page=XLoginController&locale2=vi_VN&refid=9', 'https://block.vn/a/language.php?l=pt_BR&lref=https%3A%2F%2Fm.facebook.com%2Flogin.php%3Fskip_api_login%3D1%26api_key%3D176087696194193%26signed_next%3D1%26next%3Dhttps%253A%252F%252Fm.facebook.com%252Fv2.8%252Fdialog%252Foauth%253Fredirect_uri%253Dhttps%25253A%25252F%25252Fblock.vn%25252Floginwithfacebook%2526scope%253Demail%25252Cpublic_profile%2526client_id%253D176087696194193%2526ret%253Dlogin%2526logger_id%253Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1%26cancel_url%3Dhttps%253A%252F%252Fblock.vn%252Floginwithfacebook%253Ferror%253Daccess_denied%2526error_code%253D200%2526error_description%253DPermissions%252Berror%2526error_reason%253Duser_denied%2523_%253D_%26display%3Dtouch%26locale%3Dvi_VN%26logger_id%3Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1&index=4&gfid=AQBHpqAgXgqe_KNq&ref_component=mbasic_footer&ref_page=XLoginController&locale2=vi_VN&refid=9', 'https://block.vn/a/language.php?l=en_GB&lref=https%3A%2F%2Fm.facebook.com%2Flogin.php%3Fskip_api_login%3D1%26api_key%3D176087696194193%26signed_next%3D1%26next%3Dhttps%253A%252F%252Fm.facebook.com%252Fv2.8%252Fdialog%252Foauth%253Fredirect_uri%253Dhttps%25253A%25252F%25252Fblock.vn%25252Floginwithfacebook%2526scope%253Demail%25252Cpublic_profile%2526client_id%253D176087696194193%2526ret%253Dlogin%2526logger_id%253Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1%26cancel_url%3Dhttps%253A%252F%252Fblock.vn%252Floginwithfacebook%253Ferror%253Daccess_denied%2526error_code%253D200%2526error_description%253DPermissions%252Berror%2526error_reason%253Duser_denied%2523_%253D_%26display%3Dtouch%26locale%3Dvi_VN%26logger_id%3Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1&index=1&gfid=AQC5Crz_U41IWXma&ref_component=mbasic_footer&ref_page=XLoginController&locale2=vi_VN&refid=9', 'https://block.vn/a/language.php?l=es_LA&lref=https%3A%2F%2Fm.facebook.com%2Flogin.php%3Fskip_api_login%3D1%26api_key%3D176087696194193%26signed_next%3D1%26next%3Dhttps%253A%252F%252Fm.facebook.com%252Fv2.8%252Fdialog%252Foauth%253Fredirect_uri%253Dhttps%25253A%25252F%25252Fblock.vn%25252Floginwithfacebook%2526scope%253Demail%25252Cpublic_profile%2526client_id%253D176087696194193%2526ret%253Dlogin%2526logger_id%253Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1%26cancel_url%3Dhttps%253A%252F%252Fblock.vn%252Floginwithfacebook%253Ferror%253Daccess_denied%2526error_code%253D200%2526error_description%253DPermissions%252Berror%2526error_reason%253Duser_denied%2523_%253D_%26display%3Dtouch%26locale%3Dvi_VN%26logger_id%3Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1&index=3&gfid=AQDe4IpgW2pbeNRb&ref_component=mbasic_footer&ref_page=XLoginController&locale2=vi_VN&refid=9', 'https://block.vn/language.php?n=https%3A%2F%2Fm.facebook.com%2Flogin.php%3Fskip_api_login%3D1%26api_key%3D176087696194193%26signed_next%3D1%26next%3Dhttps%253A%252F%252Fm.facebook.com%252Fv2.8%252Fdialog%252Foauth%253Fredirect_uri%253Dhttps%25253A%25252F%25252Fblock.vn%25252Floginwithfacebook%2526scope%253Demail%25252Cpublic_profile%2526client_id%253D176087696194193%2526ret%253Dlogin%2526logger_id%253Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1%26cancel_url%3Dhttps%253A%252F%252Fblock.vn%252Floginwithfacebook%253Ferror%253Daccess_denied%2526error_code%253D200%2526error_description%253DPermissions%252Berror%2526error_reason%253Duser_denied%2523_%253D_%26display%3Dtouch%26locale%3Dvi_VN%26logger_id%3Db0e4ea69-a57d-ac2d-6cec-0edd89da8ea1&ref_component=mbasic_footer&ref_page=XLoginController&locale2=vi_VN&refid=9', 'https://accounts.google.com/signin/usernamerecovery?continue=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fauth%3Fclient_id%3D683798868148-ue66l3ii7fi5r8dsd75k4ugd56ehhicm.apps.googleusercontent.com%26response_type%3Dcode%26scope%3Dopenid%2Bemail%2Bprofile%26state%3D%2Fprofile%26%26redirect_uri%3Dhttps%3A%2F%2Fblock.vn%2Floginwithgoogle%26access_type%3Doffline%26approval_prompt%3Dforce%26from_login%3D1%26as%3Dd86C42sINGragr-o7xxJ_w&sarp=1&scc=1&hl=vi', 'https://accounts.google.com/AccountChooser?continue=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fauth%3Fclient_id%3D683798868148-ue66l3ii7fi5r8dsd75k4ugd56ehhicm.apps.googleusercontent.com%26response_type%3Dcode%26scope%3Dopenid%2Bemail%2Bprofile%26state%3D%2Fprofile%26%26redirect_uri%3Dhttps%3A%2F%2Fblock.vn%2Floginwithgoogle%26access_type%3Doffline%26approval_prompt%3Dforce%26from_login%3D1%26as%3Dd86C42sINGragr-o7xxJ_w&followup=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fauth%3Fclient_id%3D683798868148-ue66l3ii7fi5r8dsd75k4ugd56ehhicm.apps.googleusercontent.com%26response_type%3Dcode%26scope%3Dopenid%2Bemail%2Bprofile%26state%3D%2Fprofile%26%26redirect_uri%3Dhttps%3A%2F%2Fblock.vn%2Floginwithgoogle%26access_type%3Doffline%26approval_prompt%3Dforce%26from_login%3D1%26as%3Dd86C42sINGragr-o7xxJ_w&scc=1&sarp=1&oauth=1', 'https://accounts.google.com/SignUp?continue=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fauth%3Fclient_id%3D683798868148-ue66l3ii7fi5r8dsd75k4ugd56ehhicm.apps.googleusercontent.com%26response_type%3Dcode%26scope%3Dopenid%2Bemail%2Bprofile%26state%3D%2Fprofile%26%26redirect_uri%3Dhttps%3A%2F%2Fblock.vn%2Floginwithgoogle%26access_type%3Doffline%26approval_prompt%3Dforce%26from_login%3D1%26as%3Dd86C42sINGragr-o7xxJ_w', 'https://www.google.com/intl/vi/about', 'https://accounts.google.com/TOS?loc=VN&hl=vi&privacy=true', 'https://accounts.google.com/TOS?loc=VN&hl=vi', 'http://www.google.com/support/accounts?hl=vi'], ['', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'https://block.vn/dang-ky-hoc/react-co-ban', 'http://bmag.vn', '', 'https://block.vn/dang-ky-hoc/java-exception-logging-api', 'https://block.vn/dang-ky-hoc/java-generics-collections', 'https://block.vn/dang-ky-hoc/java-regular-expression', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '']

        );

        var boxW = 300;
        var boxH = 25;

        var halfBoxH = boxH / 2;

        var nfloor = map.length;

        var mxWidth = nfloor * boxW + (nfloor - 1) * 50 + 10 + 10;
        var mxHeight = map[nfloor - 1].length * (boxH + 10) + 20;

        function addTextToBox(coordX, coordY, fontType, content) {
            context.save();
            context.fillStyle = "black";
            context.font = content;
            context.textAlign = 'left';
            context.translate(coordX + 10, coordY + boxH - 10);
            context.fillText(content, 0, 0);
            context.restore();
        }


        function initialMap() {
            // resize to fix map
            canvas.width = mxWidth;
            canvas.height = mxHeight;
        }

        var StartDrawingX = mxWidth - 10 - boxW;
        var StartDrawingY = 10;

        function buildTreeMap(floor, deep, coord) {
            // eslint-disable-next-line 
            var rect = new Array();

            if (floor === nfloor - 1) {

                var curRow = StartDrawingY;

                for (var i = 0; i < map[floor].length; i++) {
                    if (map[floor][i] > 0) {
                        context.fillStyle = "black";
                        context.fillRect(deep, curRow, boxW, boxH);
                        context.stroke();

                        // save the rectangle
                        rectCoord.push({ x: deep, y: curRow, url: urlMap[floor][i] });

                        // analysis color of type
                        context.fillStyle = "#79c5fc";
                        if (typeMap[floor][i] === 3) context.fillStyle = "red";
                        if (typeMap[floor][i] === 2) context.fillStyle = "gray";

                        context.fillRect(deep + 2, curRow + 2, boxW - 4, boxH - 4);
                        context.stroke();

                        // add text
                        addTextToBox(deep, curRow, '12px serif', urlMap[floor][i]);
                        //addTextToBox(deep, curRow, '12px serif', 'x = ' + deep + " y = " + curRow);
                    }
                    rect.push(curRow);
                    curRow = curRow + boxH + 10;
                }
            }
            else {
                for (let i = 0; i < map[floor].length; i++) {
                    if (map[floor][i] > 0) {

                        context.fillStyle = "black";
                        context.fillRect(deep, coord[i], boxW, boxH);
                        context.stroke();

                        // save the rectangle
                        rectCoord.push({ x: deep, y: coord[i], url: urlMap[floor][i] });

                        // analysis color of type
                        context.fillStyle = "#79c5fc";
                        if (typeMap[floor][i] === 3) context.fillStyle = "red";
                        if (typeMap[floor][i] === 2) context.fillStyle = "gray";


                        context.fillRect(deep + 2, coord[i] + 2, boxW - 4, boxH - 4);
                        context.stroke();

                        // add text
                        addTextToBox(deep, coord[i], '12px serif', urlMap[floor][i]);
                        //addTextToBox(deep, coord[i], '12px serif', 'x = ' + deep + "y = " + coord[i]);
                    }
                    rect.push(coord[i]);
                }
            }

            // Draw linked lines
            if (floor === 0) return;
            // eslint-disable-next-line 
            coord = new Array();
            var bgSeg = 0;
            for (let i = 0; i < map[floor].length; i++) {
                if (i === map[floor].length - 1 || map[floor][i] === 0 || (map[floor][i] !== map[floor][i + 1])) {

                    var endSeg = i;

                    if (map[floor][i] !== 0) {

                        for (var j = bgSeg; j <= endSeg; j++) {
                            context.moveTo(deep, rect[j] + halfBoxH);
                            context.lineTo(deep - 25, rect[j] + halfBoxH);
                            context.stroke();
                        }

                        // line begin to end
                        context.moveTo(deep - 25, rect[bgSeg] + halfBoxH);
                        context.lineTo(deep - 25, rect[endSeg] + halfBoxH);
                        context.stroke();

                        // line to parent
                        var mid = rect[bgSeg] + halfBoxH + (rect[endSeg] - rect[bgSeg]) / 2
                        context.moveTo(deep - 25, mid);
                        context.lineTo(deep - 50, mid);
                        context.stroke();
                    }

                    coord.push(rect[bgSeg] + (rect[endSeg] - rect[bgSeg]) / 2);
                    bgSeg = i + 1;
                }
            }

            buildTreeMap(floor - 1, deep - 50 - boxW, coord);
        }


        initialMap();
        buildTreeMap(nfloor - 1, StartDrawingX, []);

        canvas.onmousemove = function (event) {
            // var rect = canvas.getBoundingClientRect();
            // let x = event.clientX - rect.left;
            // let y = event.clientY - rect.top;

            // tracking location
            // document.getElementById("mx").innerHTML = "x = " + x + "y = " + y;
        }

        // add 'click event' to canvas
        canvas.addEventListener('click', function (event) {

            // correct the coordinate
            var rect = canvas.getBoundingClientRect();
            let x = event.clientX - rect.left;
            let y = event.clientY - rect.top;

            //alert(x + ", " + y);

            var isInside = false;
            var id = -1;
            // loop in all rectangle
            for (var i = 0; i < rectCoord.length; i++) {

                var minX = rectCoord[i].x;
                var minY = rectCoord[i].y;
                var maxX = minX + boxW - 1;
                var maxY = minY + boxH - 1;

                if (minX <= x && x <= maxX && minY <= y && y <= maxY) {
                    //alert("Inside!");
                    isInside = true;
                    id = i;
                    break;
                }
            }

            if (isInside === true) {
                // pop-up the url
                alert("Your URL: " + rectCoord[id].url);
            }
        });
    }

    render() {
        return (
            <Segment style={{ maxHeight: 550, overflow: "auto" }} >
                <canvas id="myCanvas" ref="myCanvas" >
                    Bla bla
                    </canvas>
            </Segment>
        );

    }
}