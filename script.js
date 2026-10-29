// ========================================
// FOODHUB RESTAURANT MANAGEMENT SYSTEM
// JAVASCRIPT
// ========================================


// ========================================
// FOOD MENU DATA
// ========================================

const foodItems = [

    {
        id: 1,
        name: "French Fries",
        category: "Starter",
        price: 120
    },

    {
        id: 2,
        name: "Paneer Chilli",
        category: "Starter",
        price: 180
    },

    {
        id: 3,
        name: "Veg Biryani",
        category: "Main Course",
        price: 220
    },

    {
        id: 4,
        name: "Paneer Butter Masala",
        category: "Main Course",
        price: 240
    },

    {
        id: 5,
        name: "Butter Naan",
        category: "Main Course",
        price: 50
    },

    {
        id: 6,
        name: "Cold Drink",
        category: "Beverage",
        price: 60
    },

    {
        id: 7,
        name: "Coffee",
        category: "Beverage",
        price: 90
    },

    {
        id: 8,
        name: "Ice Cream",
        category: "Dessert",
        price: 100
    }

];


// ========================================
// CART
// ========================================

let cart = JSON.parse(
    localStorage.getItem("foodHubCart")
) || [];


// ========================================
// ADD TO CART
// ========================================

function addToCart(foodId) {

    const food = foodItems.find(
        item => item.id === foodId
    );

    if (!food) {
        return;
    }


    const existingItem = cart.find(
        item => item.id === foodId
    );


    if (existingItem) {

        existingItem.quantity++;

    } 
    else {

        cart.push({

            id: food.id,

            name: food.name,

            category: food.category,

            price: food.price,

            quantity: 1

        });

    }


    saveCart();

    updateCartCount();

    alert(food.name + " added to cart!");
}


// ========================================
// SAVE CART
// ========================================

function saveCart() {

    localStorage.setItem(
        "foodHubCart",
        JSON.stringify(cart)
    );

}


// ========================================
// UPDATE CART COUNT
// ========================================

function updateCartCount() {

    const cartCount =
        document.getElementById("cartCount");


    if (!cartCount) {
        return;
    }


    let totalQuantity = 0;


    cart.forEach(item => {

        totalQuantity += item.quantity;

    });


    cartCount.textContent =
        totalQuantity;
}


// ========================================
// LOAD CART
// ========================================

function loadCart() {

    const cartContainer =
        document.getElementById("cartItems");


    if (!cartContainer) {
        return;
    }


    cartContainer.innerHTML = "";


    if (cart.length === 0) {

        cartContainer.innerHTML = `

            <div class="empty-cart">

                <div>
                    🛒
                </div>

                <h2>
                    Your Cart is Empty
                </h2>

                <p>
                    Add some delicious food
                    from our menu!
                </p>

                <a href="menu.html"
                   class="btn">

                    Explore Menu

                </a>

            </div>

        `;

        updateCartTotal();

        return;
    }


    cart.forEach(item => {

        const itemTotal =
            item.price * item.quantity;


        const cartItem =
            document.createElement("div");


        cartItem.className =
            "cart-item";


        cartItem.innerHTML = `

            <div class="cart-item-image">

                ${getFoodEmoji(item.id)}

            </div>


            <div class="cart-item-details">

                <h3>
                    ${item.name}
                </h3>

                <p>
                    ${item.category}
                </p>

                <span>
                    ₹${item.price} each
                </span>

            </div>


            <div class="quantity-control">

                <button
                    onclick="decreaseQuantity(${item.id})">

                    −

                </button>


                <span>
                    ${item.quantity}
                </span>


                <button
                    onclick="increaseQuantity(${item.id})">

                    +

                </button>

            </div>


            <div class="cart-item-price">

                ₹${itemTotal.toFixed(2)}

            </div>


            <button
                class="remove-btn"
                onclick="removeFromCart(${item.id})">

                ✕

            </button>

        `;


        cartContainer.appendChild(
            cartItem
        );

    });


    updateCartTotal();
}


// ========================================
// FOOD EMOJI
// ========================================

function getFoodEmoji(id) {

    const emojis = {

        1: "🍟",

        2: "🌶️",

        3: "🍛",

        4: "🍲",

        5: "🫓",

        6: "🥤",

        7: "☕",

        8: "🍨"

    };


    return emojis[id] || "🍽️";
}


// ========================================
// INCREASE QUANTITY
// ========================================

function increaseQuantity(foodId) {

    const item = cart.find(
        item => item.id === foodId
    );


    if (item) {

        item.quantity++;

    }


    saveCart();

    loadCart();

    updateCartCount();
}


// ========================================
// DECREASE QUANTITY
// ========================================

function decreaseQuantity(foodId) {

    const item = cart.find(
        item => item.id === foodId
    );


    if (!item) {
        return;
    }


    if (item.quantity > 1) {

        item.quantity--;

    } 
    else {

        removeFromCart(foodId);

        return;

    }


    saveCart();

    loadCart();

    updateCartCount();
}


// ========================================
// REMOVE ITEM
// ========================================

function removeFromCart(foodId) {

    cart = cart.filter(
        item => item.id !== foodId
    );


    saveCart();

    loadCart();

    updateCartCount();
}


// ========================================
// CALCULATE SUBTOTAL
// ========================================

function calculateSubtotal() {

    let subtotal = 0;


    cart.forEach(item => {

        subtotal +=
            item.price * item.quantity;

    });


    return subtotal;
}


// ========================================
// CALCULATE DISCOUNT
// ========================================

function calculateDiscount(subtotal) {

    let discount = 0;


    if (subtotal >= 2000) {

        discount =
            subtotal * 0.15;

    } 
    else if (subtotal >= 1000) {

        discount =
            subtotal * 0.10;

    } 
    else if (subtotal >= 500) {

        discount =
            subtotal * 0.05;

    }


    return discount;
}


// ========================================
// CALCULATE GST
// ========================================

function calculateGST(amount) {

    return Math.round(amount * 0.05 * 100) / 100;

}


// ========================================
// UPDATE CART TOTAL
// ========================================

function updateCartTotal() {

    const subtotal =
        calculateSubtotal();


    const discount =
        calculateDiscount(subtotal);


    const afterDiscount =
        subtotal - discount;


    const gst =
        calculateGST(afterDiscount);


    const grandTotal =
        afterDiscount + gst;


    const subtotalElement =
        document.getElementById(
            "cartSubtotal"
        );


    const discountElement =
        document.getElementById(
            "cartDiscount"
        );


    const gstElement =
        document.getElementById(
            "cartGST"
        );


    const totalElement =
        document.getElementById(
            "cartGrandTotal"
        );


    if (subtotalElement) {

        subtotalElement.textContent =
            "₹" + subtotal.toFixed(2);

    }


    if (discountElement) {

        discountElement.textContent =
            "₹" + discount.toFixed(2);

    }


    if (gstElement) {

        gstElement.textContent =
            "₹" + gst.toFixed(2);

    }


    if (totalElement) {

        totalElement.textContent =
            "₹" + grandTotal.toFixed(2);

    }

}


// ========================================
// MENU FILTER
// ========================================

function filterMenu(category) {

    const foodCards =
        document.querySelectorAll(
            ".food-card"
        );


    foodCards.forEach(card => {

        const cardCategory =
            card.dataset.category;


        if (
            category === "all" ||
            cardCategory === category
        ) {

            card.style.display =
                "block";

        } 
        else {

            card.style.display =
                "none";

        }

    });

}

// ========================================
// PLACE ORDER
// ========================================

async function placeOrder() {

    // Check whether cart has items

    if (cart.length === 0) {

        alert(
            "Your cart is empty. Please add items before placing the order."
        );

        window.location.href = "menu.html";

        return;
    }


    // Get customer details

    const name =
        document.getElementById("customerName")?.value.trim();

    const mobile =
        document.getElementById("mobile")?.value.trim();

    const table =
        document.getElementById("tableNumber")?.value.trim();

    const payment =
        document.getElementById("paymentMethod")?.value;


    // Validate name

    if (!name) {

        alert("Please enter customer name.");

        return;
    }


    // Validate mobile

    if (!mobile) {

        alert("Please enter mobile number.");

        return;
    }


    if (!/^[0-9]{10}$/.test(mobile)) {

        alert(
            "Please enter a valid 10-digit mobile number."
        );

        return;
    }


    // Validate table

    if (!table) {

        alert("Please enter table number.");

        return;
    }


    // Validate payment

    if (!payment) {

        alert("Please select a payment method.");

        return;
    }


    // ========================================
    // CALCULATE BILL
    // ========================================

    const subtotal =
        calculateSubtotal();

    const discount =
        calculateDiscount(subtotal);

    const amountAfterDiscount =
        subtotal - discount;

    const gst =
        calculateGST(amountAfterDiscount);

    const grandTotal =
        amountAfterDiscount + gst;


    // ========================================
    // GENERATE ORDER NUMBER
    // ========================================

    const orderNumber =
        "FH" +
        Date.now().toString().slice(-6);


    // ========================================
    // CREATE FINAL ORDER
    // ========================================

    const finalOrder = {

        orderNumber: orderNumber,

        customerName: name,

        mobile: mobile,

        tableNumber: table,

        paymentMethod: payment,

        paymentStatus: "PAID",

        items: cart,

        subtotal:
            Number(subtotal.toFixed(2)),

        discount:
            Number(discount.toFixed(2)),

        gst:
            Number(gst.toFixed(2)),

        grandTotal:
            Number(grandTotal.toFixed(2)),

        orderDate:
            new Date().toLocaleString("en-IN")

    };


    // ========================================
    // DISABLE PLACE ORDER BUTTON
    // ========================================

    const placeOrderButton =
        document.querySelector(".place-order-btn");

    if (placeOrderButton) {

        placeOrderButton.disabled = true;

        placeOrderButton.textContent =
            "Processing Order...";

    }


    // ========================================
    // SEND ORDER TO JAVA BACKEND
    // ========================================

    try {

        const response =
            await fetch(
                "http://localhost:8080/api/order",
                {

                    method: "POST",

                    headers: {
                        "Content-Type": "text/plain"
                    },

                    body:
                        JSON.stringify(finalOrder)

                }
            );


        // Read Java response

        const data =
            await response.json();


        console.log(
            "Java Backend Response:",
            data
        );


        // ========================================
        // CHECK JAVA RESPONSE
        // ========================================

        if (!response.ok) {

            throw new Error(
                "Java server returned an error."
            );
        }


        // ========================================
        // SAVE COMPLETED ORDER
        // ========================================

        localStorage.setItem(
            "foodHubFinalOrder",
            JSON.stringify(finalOrder)
        );


        // Save as previous order

        localStorage.setItem(
            "foodHubOrder",
            JSON.stringify(finalOrder)
        );


        // ========================================
        // CLEAR SHOPPING CART
        // ========================================

        cart = [];

        saveCart();

        updateCartCount();


        // ========================================
        // SUCCESS MESSAGE
        // ========================================

        alert(
            "🎉 Order placed successfully!\n\n" +

            "Order Number: " +
            orderNumber +

            "\nPayment: " +
            payment +

            "\nAmount: ₹" +
            grandTotal.toFixed(2)
        );


        // ========================================
        // OPEN FINAL BILL
        // ========================================

        window.location.href =
            "bill.html";


    }
    catch (error) {

        // ========================================
        // JAVA BACKEND ERROR
        // ========================================

        console.error(
            "Java Backend Connection Error:",
            error
        );


        alert(
            "❌ Could not connect to Java backend.\n\n" +
            "Please make sure the Java server is running."
        );


        // Enable button again

        if (placeOrderButton) {

            placeOrderButton.disabled = false;

            placeOrderButton.textContent =
                "Place Order";

        }

    }

}

// ========================================
// PAGE LOAD
// ========================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadCart();

        updateCartCount();

    }
);

// ========================================
// LOAD FINAL BILL
// ========================================

function loadBill() {

    const billItems =
        document.getElementById("billItems");


    if (!billItems) {
        return;
    }


    // Check for completed order first

    const savedFinalOrder =
        JSON.parse(
            localStorage.getItem("foodHubFinalOrder")
        );


    // Check normal cart

    const currentCart = cart;


    // If completed order exists,
    // show the completed order

    if (savedFinalOrder) {

        displayFinalOrder(savedFinalOrder);

        return;
    }


    // If cart is empty

    if (currentCart.length === 0) {

        billItems.innerHTML = `

            <p>
                Your cart is empty.
            </p>

        `;

        return;
    }


    // Display current cart

    billItems.innerHTML = "";


    currentCart.forEach(item => {

        const itemTotal =
            item.price * item.quantity;


        const billItem =
            document.createElement("div");


        billItem.className =
            "bill-item";


        billItem.innerHTML = `

            <span>
                ${item.name} × ${item.quantity}
            </span>

            <span>
                ₹${itemTotal.toFixed(2)}
            </span>

        `;


        billItems.appendChild(
            billItem
        );

    });


    // Calculate bill

    const subtotal =
        calculateSubtotal();


    const discount =
        calculateDiscount(subtotal);


    const amountAfterDiscount =
        subtotal - discount;


    const gst =
        calculateGST(amountAfterDiscount);


    const grandTotal =
        amountAfterDiscount + gst;


    // Display amounts

    document.getElementById(
        "billSubtotal"
    ).textContent =
        "₹" + subtotal.toFixed(2);


    document.getElementById(
        "billDiscount"
    ).textContent =
        "- ₹" + discount.toFixed(2);


    document.getElementById(
        "billGST"
    ).textContent =
        "₹" + gst.toFixed(2);


    document.getElementById(
        "billGrandTotal"
    ).textContent =
        "₹" + grandTotal.toFixed(2);


    // Date

    const dateElement =
        document.getElementById("billDate");


    if (dateElement) {

        dateElement.textContent =
            new Date().toLocaleDateString(
                "en-IN"
            );

    }


    // Payment status

    const paymentStatus =
        document.getElementById(
            "paymentStatus"
        );


    if (paymentStatus) {

        paymentStatus.textContent =
            "PENDING";

    }

}


// ========================================
// DISPLAY COMPLETED ORDER
// ========================================

function displayFinalOrder(order) {

    const billItems =
        document.getElementById("billItems");


    if (!billItems) {
        return;
    }


    // Clear old items

    billItems.innerHTML = "";


    // Display each item

    order.items.forEach(item => {

        const itemTotal =
            item.price * item.quantity;


        const billItem =
            document.createElement("div");


        billItem.className =
            "bill-item";


        billItem.innerHTML = `

            <span>
                ${item.name} × ${item.quantity}
            </span>

            <span>
                ₹${itemTotal.toFixed(2)}
            </span>

        `;


        billItems.appendChild(
            billItem
        );

    });


    // Bill number

    const billNumber =
        document.getElementById(
            "billNumber"
        );


    if (billNumber) {

        billNumber.textContent =
            order.orderNumber;

    }


    // Date

    const billDate =
        document.getElementById(
            "billDate"
        );


    if (billDate) {

        billDate.textContent =
            order.orderDate;

    }


    // Amounts

    document.getElementById(
        "billSubtotal"
    ).textContent =
        "₹" + order.subtotal.toFixed(2);


    document.getElementById(
        "billDiscount"
    ).textContent =
        "- ₹" + order.discount.toFixed(2);


    document.getElementById(
        "billGST"
    ).textContent =
        "₹" + order.gst.toFixed(2);


    document.getElementById(
        "billGrandTotal"
    ).textContent =
        "₹" + order.grandTotal.toFixed(2);


    // Payment status

    const paymentStatus =
        document.getElementById(
            "paymentStatus"
        );


    if (paymentStatus) {

        paymentStatus.textContent =
            "PAID";

    }


    // Update customer information

    const nameInput =
        document.getElementById(
            "customerName"
        );


    const mobileInput =
        document.getElementById(
            "mobile"
        );


    const tableInput =
        document.getElementById(
            "tableNumber"
        );


    const paymentInput =
        document.getElementById(
            "paymentMethod"
        );


    if (nameInput) {

        nameInput.value =
            order.customerName;

    }


    if (mobileInput) {

        mobileInput.value =
            order.mobile;

    }


    if (tableInput) {

        tableInput.value =
            order.tableNumber;

    }


    if (paymentInput) {

        paymentInput.value =
            order.paymentMethod;

    }
        const placeOrderButton =
        document.querySelector(".place-order-btn");


    if (placeOrderButton) {

        placeOrderButton.textContent =
            "✓ Order Completed";

        placeOrderButton.disabled = true;

        placeOrderButton.style.cursor =
            "not-allowed";

        placeOrderButton.style.opacity =
            "0.7";

    }

}


// ========================================
// LOAD CUSTOMER DETAILS
// ========================================

function loadCustomerDetails() {

    const order =
        JSON.parse(
            localStorage.getItem(
                "foodHubOrder"
            )
        );


    if (!order) {
        return;
    }


    const nameInput =
        document.getElementById(
            "customerName"
        );


    const mobileInput =
        document.getElementById(
            "mobile"
        );


    const tableInput =
        document.getElementById(
            "tableNumber"
        );


    const paymentInput =
        document.getElementById(
            "paymentMethod"
        );


    if (nameInput) {

        nameInput.value =
            order.customerName || "";

    }


    if (mobileInput) {

        mobileInput.value =
            order.mobile || "";

    }


    if (tableInput) {

        tableInput.value =
            order.tableNumber || "";

    }


    if (paymentInput) {

        paymentInput.value =
            order.paymentMethod || "cash";

    }

}


// ========================================
// RUN BILL WHEN PAGE LOADS
// ========================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadBill();

    }
);