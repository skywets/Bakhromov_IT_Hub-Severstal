
<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample"
   role="button" aria-expanded="false" aria-controls="collapseExample">
    Обновить заметку
</a>
<div class="collapse <#if note??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                       value="<#if note??>${note.text}</#if>" name="text" placeholder="Текст заметки" />
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control"
                       value="<#if note??>${note.tag}</#if>" name="tag" placeholder="Тэг заметки">
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id" value="<#if note??>${note.id}</#if>" />
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Обновить</button>
            </div>
        </form>
    </div>
</div>
