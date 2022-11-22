let isSubscribed = false;
let swRegistration = null;
let applicationKey = 'BC3nw-aB9DIs4A823ASQp8MN5dqgLYK0zOcZOq7ltbhmzhJ91XYWwrAUJAztuOiSrM3Xhnh4ZgqPrzxG4ZYqv3A';

// Url Encription
function urlB64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - (base64String.length % 4)) % 4);
  const base64 = (base64String + padding).replace(/\-/g, '+').replace(/_/g, '/');

  const rawData = window.atob(base64);
  const outputArray = new Uint8Array(rawData.length);

  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
}

// Installing service worker
if ('serviceWorker' in navigator && 'PushManager' in window) {
  navigator.serviceWorker
    .register('content/assets/js/service-worker.js')
    .then(function (swReg) {
      swRegistration = swReg;
      swRegistration.pushManager.getSubscription().then(function (subscription) {
        isSubscribed = !(subscription === null);
        if (isSubscribed) {
          // eslint-disable-next-line no-console
          console.log('Abonné au Push');
        } else {
          swRegistration.pushManager
            .subscribe({
              userVisibleOnly: true,
              applicationServerKey: urlB64ToUint8Array(applicationKey),
            })
            .then(function (subscription) {
              saveSubscription(subscription);
              isSubscribed = true;
            })
            .catch(function (err) {
              // eslint-disable-next-line no-console
              console.log('Erreur abonnement: ', err);
            });
        }
      });
    })
    .catch(function (error) {
      // eslint-disable-next-line no-console
      console.error('Service Worker Erreur', error);
    });
} else {
  // eslint-disable-next-line no-console
  console.warn('Push messaging insupporté');
}

// Send request to database for add new subscriber
function saveSubscription(subscription) {
  let xmlHttp = new XMLHttpRequest();

  xmlHttp.open('POST', 'http://localhost:3000/subscribe');
  xmlHttp.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
  xmlHttp.onreadystatechange = function () {
    if (xmlHttp.readyState != 4) return;
    if (xmlHttp.status != 200 && xmlHttp.status != 304) {
      console.log('HTTP error ' + xmlHttp.status, null);
    } else {
      this.registration.showNotification('Play2win Info', {
        body: "Merci pour l'inscription au push",
        icon: 'https://simvas.com/logo-play2win-PushNotification.png',
        image: 'https://simvas.com/logo-play2win-PushNotification.png',
        badge: 'https://simvas.com/logo-play2win-PushNotification.png',
        tag: 'Play2win',
      });
      // eslint-disable-next-line no-console
      console.log('Abonné au Push');
    }
  };

  xmlHttp.send(JSON.stringify(subscription));
}
