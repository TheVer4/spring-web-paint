<#import "parts/common.ftl" as c>
<#import "parts/loginForm.ftl" as lf>
<@c.page>
Регистрация
<#if message??>
    ${message}
</#if>
<@lf.login "/registration"/>
</@c.page>