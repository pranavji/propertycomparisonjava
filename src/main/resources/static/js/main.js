function copyContent(id,mode) {
  /* Get the text field */
  var copyText = document.getElementById(id);

  /* Select the text field */
  copyText.select();
  copyText.setSelectionRange(0, 99999); /*For mobile devices*/

  if(mode)
  {  /* Copy the text inside the text field */
     document.execCommand("copy");

     /* Alert the copied text */
     alert("Copied the text to clipboard");
   }
   return false;
   }