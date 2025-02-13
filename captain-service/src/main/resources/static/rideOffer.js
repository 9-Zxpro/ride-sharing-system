
    // TODO: Inject it dynamically from backend
    const captainId = 1;
    document.getElementById('captainIdDisplay').textContent = captainId;

    // Connect to the WebSocket endpoint (assumed to be at /ws)
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
      console.log('Connected: ' + frame);
      // Subscribe to the captain's specific topic
      stompClient.subscribe('/topic/captain/' + captainId, function(message) {
        const offer = JSON.parse(message.body);
        displayOffer(offer);
      });
    });

    // Function to display ride offer details with Accept/Reject buttons
    function displayOffer(offer) {
      const offersDiv = document.getElementById('offers');

      // Create a container for the offer
      const offerDiv = document.createElement('div');
      offerDiv.className = 'offer';

      offerDiv.innerHTML = `
        <p><strong>Ride ID:</strong> ${offer.rideId}</p>
        <p><strong>Pickup Address:</strong> ${offer.pickupAddress}</p>
        <p><strong>Dropoff Address:</strong> ${offer.dropoffAddress}</p>
        <p><strong>Pickup Distance:</strong> ${offer.pickupDistance} meters</p>
        <p><strong>Fare:</strong> ${offer.fare}</p>
        <button onclick="acceptOffer('${offer.rideId}', ${offer.captainId})">Accept</button>
        <button onclick="rejectOffer('${offer.rideId}', ${offer.captainId})">Reject</button>
      `;

      offersDiv.appendChild(offerDiv);
    }

    // Function to accept a ride offer
    function acceptOffer(rideId, captainId) {
      fetch(`/api/captains/${captainId}/accept/${rideId}`, {
        method: 'POST'
      })
      .then(response => response.json())
      .then(data => {
        alert(data.message);
        // Optionally, remove the offer from the UI if accepted.
        removeOfferFromUI(rideId);
      })
      .catch(error => {
        console.error('Error accepting offer:', error);
        alert('Error accepting offer');
      });
    }

    // Function to reject a ride offer
    function rejectOffer(rideId, captainId) {
      // Optionally, you can also send a rejection API call if required.
      alert('Offer rejected for Ride ID ' + rideId);
      removeOfferFromUI(rideId);
    }

    // Helper function to remove an offer element from the UI
    function removeOfferFromUI(rideId) {
      const offersDiv = document.getElementById('offers');
      // Remove the offer element that matches the rideId
      Array.from(offersDiv.children).forEach(child => {
        if (child.innerHTML.includes(`Ride ID:</strong> ${rideId}`)) {
          offersDiv.removeChild(child);
        }
      });
    }