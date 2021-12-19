<#import "parts/common.ftl" as c>
<#import "parts/loginForm.ftl" as lf>
<@c.page>
    <@lf.logout/>
    ${board.boardName} created by ${board.owner.getUsername()}
    <div id="container"></div>
    <input type="hidden" id="CSRF" value="${ _csrf.token }">
    <input type="button" id="addLine" value="Add line">
    <input type="button" id="addText" value="Add text (then click on canvas)">
    <input type="button" id="clearCanvas" value="Clear all">
    <script src="paint.js"></script>
</@c.page>