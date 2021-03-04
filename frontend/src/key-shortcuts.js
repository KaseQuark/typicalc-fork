document.onkeydown = handleKey;

function handleKey(e) {
    if (e.target.tagName.toLowerCase() === "vaadin-text-field") {
        return;
    }
    if (e.keyCode === 37) {
        // left arrow
        if (!e.ctrlKey) {
            document.getElementById("previous-step").click();
        } else {
            document.getElementById("first-step").click();
        }
    } else if (e.keyCode === 39) {
        // right arrow
        if (!e.ctrlKey) {
            document.getElementById("next-step").click();
        } else {
            document.getElementById("last-step").click();
        }
    }
}
