// $(document).ready(function() {
// // buton install
// let deferredPrompt;

// const addBtn = document.querySelector('.add-button');
// const ribranDownload = document.querySelector('.ribranDownload');
// if(ribranDownload !== null){
//     ribranDownload.style.display = 'none';
// }else{
//     ribranDownload=$("#ribranDownload")[0];
//     $("#ribranDownload")[0].style.display = 'none';
// }

// window.addEventListener('beforeinstallprompt', (e) => {

//     // Prevent Chrome 67 and earlier from automatically showing the prompt
//     e.preventDefault();
//     // Stash the event so it can be triggered later.
//     deferredPrompt = e;
//     // Update UI to notify the user they can add to home screen
//     ribranDownload.style.display = 'block';

//     addBtn.addEventListener('click', (e) => {
//         // hide our user interface that shows our A2HS button
//         ribranDownload.style.display = 'none';
//         // Show the prompt
//         deferredPrompt.prompt();
//         // Wait for the user to respond to the prompt
//         deferredPrompt.userChoice.then((choiceResult) => {
//             if (choiceResult.outcome === 'accepted') {
//                 console.log('User accepted the A2HS prompt');
//             } else {
//                 console.log('User dismissed the A2HS prompt');
//             }
//             deferredPrompt = null;
//         });
//     });
// });
// });
