// Dynamic Data
const offers = [
  {
    id: 1,
    price: 180,
    discount: 36,
    paymentMode: "Online",
    pickup: {
      location: "Road No. 36, Jubilee Hills",
      address: "#326, Jubilee Hills Main Rd, Hyderabad",
      distance: "1.8 Km",
    },
    drop: {
      location: "Cyber Towers, Hitech City",
      address: "Hitech City Rd. #478, Hitech City, Hyderabad",
      distance: "9.6 Km",
    },
  },
  {
    id: 2,
    price: 210,
    discount: 45,
    paymentMode: "Cash",
    pickup: {
      location: "Begumpet Metro Station",
      address: "Begumpet Rd, Hyderabad",
      distance: "2.2 Km",
    },
    drop: {
      location: "Banjara Hills, Hyderabad",
      address: "Banjara Hills Rd. #472, Hyderabad",
      distance: "8.3 Km",
    },
  },
  {
    id: 3,
    price: 110,
    discount: 35,
    paymentMode: "Cash",
    pickup: {
      location: "Pari Chowk Metro Station",
      address: "Tughalpur",
      distance: "2.2 Km",
    },
    drop: {
      location: "Alpha-2 Metro Station",
      address: "Alpha-2 commercial",
      distance: "8.3 Km",
    },
  },
];

// Elements
const offerList = document.querySelector(".sidebar"); // Sidebar container
const offerDetails = document.querySelector(".main-content"); // Main content container

// Render Offers in Sidebar
offers.forEach((offer, index) => {
  const offerItem = document.createElement("div");
  offerItem.className = `flex items-center space-x-2 p-2 bg-white rounded-lg shadow hover:bg-gray-100 cursor-pointer
  ${index === 0 ? "border-2 border-green-500" : ""}`;
  offerItem.innerHTML = `
    <img src="https://storage.googleapis.com/a1aa/image/Ti4K-B-6EWiNOzbjFX4JdUQPA53OwwZSgerKtdSVbqA.jpg" alt="Car icon" class="w-6 h-6" />
    <div>
      <div class="text-lg font-semibold">₹${offer.price}</div>
      <div class="text-green-500">₹${offer.discount}</div>
    </div>
  `;

  // Add click listener for showing details
  offerItem.addEventListener("click", () => showOfferDetails(index));

  offerList.appendChild(offerItem);
  console.log(offerList)
});

// Show Offer Details
function showOfferDetails(index) {
  const selectedOffer = offers[index];

  // Render selected offer details
  offerDetails.innerHTML = `
    <div class="flex items-center justify-between">
      <div class="flex items-center space-x-2">
        <img src="https://storage.googleapis.com/a1aa/image/Ti4K-B-6EWiNOzbjFX4JdUQPA53OwwZSgerKtdSVbqA.jpg" alt="Car icon" class="w-6 h-6" />
        <div class="text-lg font-semibold">
          ₹${selectedOffer.price} + <span class="text-green-500">₹${selectedOffer.discount}</span>
        </div>
      </div>
      <div class="text-gray-500">(${selectedOffer.paymentMode})</div>
    </div>
    <div class="mt-4">
      <div class="flex items-center space-x-2">
        <div class="w-4 h-4 bg-green-500 rounded-full"></div>
        <div>
          <div class="font-semibold">${selectedOffer.pickup.location}</div>
          <div class="text-gray-500 text-sm">${selectedOffer.pickup.address}</div>
        </div>
      </div>
      <div class="flex items-center space-x-2 mt-4">
        <div class="w-4 h-4 bg-red-500 rounded-full"></div>
        <div>
          <div class="font-semibold">${selectedOffer.drop.location}</div>
          <div class="text-gray-500 text-sm">${selectedOffer.drop.address}</div>
        </div>
      </div>
    </div>
    <div class="flex justify-between mt-4">
      <div>
        <div class="text-gray-500 text-sm">Pickup</div>
        <div class="font-semibold">${selectedOffer.pickup.distance}</div>
      </div>
      <div>
        <div class="text-gray-500 text-sm">Drop</div>
        <div class="font-semibold">${selectedOffer.drop.distance}</div>
      </div>
    </div>
    <div class="flex items-center justify-between mt-4">
      <button class="flex items-center justify-center w-12 h-12 bg-gray-200 rounded-full">
        <i class="fas fa-times text-xl"></i>
      </button>
      <button class="flex items-center justify-center w-32 h-12 bg-yellow-500 text-white font-semibold rounded-full">
        Accept
        <span class="ml-2 bg-white text-black rounded-full w-6 h-6 flex items-center justify-center">3</span>
      </button>
    </div>
  `;

  // Update Sidebar Selection
  const offerItems = offerList.children;
  for (let i = 0; i < offerItems.length; i++) {
    offerItems[i].className = `flex items-center space-x-2 p-2 bg-white rounded-lg shadow hover:bg-gray-100 cursor-pointer ${
      i === index ? "border-2 border-green-500" : ""
    }`;
  }
}

// Default Display
showOfferDetails(0);