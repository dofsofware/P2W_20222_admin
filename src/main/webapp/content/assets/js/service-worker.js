let notificationUrl = '';
let clients;

this.addEventListener('push', function (event) {
  const _data = event.data ? JSON.parse(event.data.text()) : {};
  notificationUrl = _data.url;
  event.waitUntil(
    this.registration.showNotification(_data.title, {
      body: _data.message,
      icon: _data.icon,
      image: _data.image,
      badge: _data.badge,
      tag: _data.tag,
    })
  );
});

this.addEventListener('notificationclick', function (event) {
  event.notification.close();

  event.waitUntil(
    clients
      .matchAll({
        type: 'window',
      })
      .then(function () {
        if (clients.openWindow) {
          return clients.openWindow(notificationUrl);
        }
      })
  );
});
