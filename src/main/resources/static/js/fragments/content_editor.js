$(document).ready(function() {
    $('#ce-upload-file').on('change', function() {
        $('#ce-textarea').focus();
        let cursorPos = $('#ce-textarea').prop('selectionStart');
        let text = $('#ce-textarea').val();
        let textBefore = text.substring(0, cursorPos);
        let textAfter = text.substring(cursorPos, text.length);
        let formData = new FormData();
        formData.append('_csrf', $('#csrf-token').attr('value'));
        formData.append('file', $('#ce-upload-file').prop('files')[0]);
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/upload',
            data: formData,
            success: function(data) {
                $('#ce-textarea').val(textBefore + `\n\t${data}\n` + textAfter);
            },
            contentType: false,
            processData: false
        });
    })
});