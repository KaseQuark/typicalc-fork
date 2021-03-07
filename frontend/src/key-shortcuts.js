document.onkeydown = handleKey;

function handleKey(e) {
    if (e.target.tagName.toLowerCase() === "vaadin-text-field") {
        return;
    }
    let element = null;
    if (e.keyCode === 37) {
        // left arrow
        if (!e.ctrlKey) {
            element = document.getElementById("previous-step");
        } else {
            element = document.getElementById("first-step");
        }
    } else if (e.keyCode === 39) {
        // right arrow
        if (!e.ctrlKey) {
            element = document.getElementById("next-step");
        } else {
            element = document.getElementById("last-step");
        }
    } else if (e.key === "/") {
        document.getElementById("inputField").focus();
        e.preventDefault();
    }
    if (element !== null) {
        element.click();
    }
}
