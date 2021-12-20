<#import "parts/common.ftl" as c>
<#import "parts/loginForm.ftl" as lf>
<@c.page>
    <@lf.logout/>
    <form method="post">
        <input type="text" name="boardName" placeholder="Введите название доски"/>
        <input type="hidden" name="_csrf" value="${ _csrf.token }"/>
        <input type="submit" value="Создать"/>
    </form>
    <div>Список досок:</div>
    <div id="board-list">
    <#list boards as board>
        <div>
            <b>${board.id}</b>
            <a href="/board?id=${board.id}">${board.boardName}</a>
            <span>by @${board.owner.getUsername()}</span>
        </div>
        <#else>
        No any boards created yet
    </#list>
    </div>
</@c.page>