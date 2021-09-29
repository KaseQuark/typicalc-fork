// utility function to copy e.g. the LaTeX code into the user's clipboard
// @ts-ignore
window.copyToClipboard = (text: string) => {
	const textarea = document.createElement("textarea");
	textarea.value = text;
	textarea.style.position = "absolute";
	textarea.style.opacity = "0";
	document.body.appendChild(textarea);
	textarea.select();
	document.execCommand("copy");
	document.body.removeChild(textarea);
};
