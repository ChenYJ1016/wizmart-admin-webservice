<#-- image.ftl -->
<div class="product-image">
    <#if imageUrl?has_content>
        <img src="${imageUrl}" alt="Product Image" style="max-width: 100px;">
    <#else>
        No image available
    </#if>
</div>
