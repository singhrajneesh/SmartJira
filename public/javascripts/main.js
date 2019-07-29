document.getElementById("fileUpload").onchange = function () {
    document.getElementById("fileUploadUrl").value = this.value.replace(/C:\\fakepath\\/i, '');
};

document.getElementById("uploadBtn").onchange = function () {
    document.getElementById("uploadFile").value = this.files[0].name;
};
