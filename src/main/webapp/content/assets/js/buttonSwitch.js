// $(document).ready(function() {
//   var nTimer = setInterval(function() {
//     if (window.jQuery) {
//       ButtonSwitcher();
//       clearInterval(nTimer);
//     }
//   }, 200);

// });

// function ButtonSwitcher() {
//   var button = document.getElementById('button-container');
//   var credit = document.getElementById('credit');
//   var internet = document.getElementById('internet');
//   var buttonTrack = document.getElementById('my-button');

//   var where = document.getElementById('for-button');

//   var buttonState = true;
//   button.addEventListener('click', function () {
//     if (buttonState) {
//       document.getElementById("my-button").style.transform = "translateX(150px)";
//       buttonState = false;
//       credit.innerText = 'internet';
//       internet.innerText = 'minutes';
//       internet.style.transform = "translateX(-150px)";
//       where.value = 'internet';
//       buttonTrack.style.borderRadius = "0px 20px 20px 0px";

//     } else {
//       document.getElementById("my-button").style.transform = "translateX(0px)";
//       buttonState = true;
//       credit.innerText = 'minutes';
//       internet.innerText = 'internet';
//       internet.style.transform = "translateX(0px)";
//       where.value = 'minutes';
//       buttonTrack.style.borderRadius = "20px 0px 0px 20px";
//     }

//   });
// }
