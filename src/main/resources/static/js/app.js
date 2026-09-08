// ==========================================
// GLOBAL VARIABLES
// ==========================================

let rooms = [];

let selectedRoom = null;



// ==========================================
// SHORTCUT
// ==========================================

function $(id) {

    return document.getElementById(id);

}



// ==========================================
// DATE FUNCTIONS
// ==========================================

function formatDate(date) {

    date.setMinutes(
        date.getMinutes()
        -
        date.getTimezoneOffset()
    );

    return date
        .toISOString()
        .split("T")[0];
}


function today() {

    return formatDate(
        new Date()
    );
}


function tomorrow() {

    const date =
        new Date();

    date.setDate(
        date.getDate() + 1
    );

    return formatDate(
        date
    );
}



// ==========================================
// TOAST MESSAGE
// ==========================================

function toast(message) {

    const element =
        $("toast");


    element.textContent =
        message;


    element.classList.add(
        "show"
    );


    setTimeout(
        function () {

            element.classList.remove(
                "show"
            );

        },
        3000
    );
}



// ==========================================
// SCROLL TO ROOMS
// ==========================================

function scrollToRooms() {

    $("rooms").scrollIntoView({
        behavior: "smooth"
    });
}



// ==========================================
// LOAD AVAILABLE ROOMS
// ==========================================

async function loadRooms() {

    const checkIn =
        $("checkIn").value;


    const checkOut =
        $("checkOut").value;


    if (
        !checkIn ||
        !checkOut
    ) {

        toast(
            "Please select your dates."
        );

        return;
    }


    if (
        new Date(checkOut)
        <=
        new Date(checkIn)
    ) {

        toast(
            "Check-out must be after check-in."
        );

        return;
    }


    $("roomGrid").innerHTML =
        "<p>Checking live availability...</p>";


    const type =
        $("roomType").value;


    try {

        const response =
            await fetch(
                `/api/rooms?checkIn=${checkIn}&checkOut=${checkOut}&type=${type}`
            );


        const data =
            await response.json();


        if (!response.ok) {

            throw new Error(
                data.error
            );
        }


        rooms =
            data;


        renderRooms();


    } catch (error) {

        $("roomGrid").innerHTML =
            "<p>Unable to connect to the Java server.</p>";

        toast(
            error.message
        );
    }
}



// ==========================================
// DISPLAY ROOMS
// ==========================================

function renderRooms() {

    if (
        rooms.length === 0
    ) {

        $("roomGrid").innerHTML =

            `<p>
                No rooms are available
                for the selected dates.
            </p>`;

        return;
    }


    $("roomGrid").innerHTML =

        rooms.map(
            function (room) {

                return `

                <article class="room">

                   <div class="photo ${room.roomType.toLowerCase()}">

    <div class="css-room">

        <div class="room-window">
            <div class="window-cross-v"></div>
            <div class="window-cross-h"></div>
        </div>

        <div class="room-bed">
            <div class="bed-head"></div>
            <div class="bed-pillow"></div>
            <div class="bed-sheet"></div>
            <div class="bed-frame"></div>
        </div>

        <div class="room-table">
            <div class="table-lamp"></div>
        </div>

    </div>

    <span class="badge">
        ● AVAILABLE
    </span>

</div>

                    <div class="room-info">

                        <small>
                            ${room.roomType}
                        </small>


                        <h3>

                            Room
                            ${room.roomNumber}

                        </h3>


                        <p>

                            ${room.description}

                        </p>


                        <div class="room-bottom">

                            <strong>

                                ₹${Number(
                                    room.pricePerNight
                                ).toLocaleString()}

                                <small>
                                    / night
                                </small>

                            </strong>


                            <button
                                class="reserve"
                                onclick="openBooking(${room.id})">

                                Reserve →

                            </button>

                        </div>

                    </div>

                </article>

                `;

            }
        ).join("");
}



// ==========================================
// OPEN BOOKING MODAL
// ==========================================

function openBooking(roomId) {

    selectedRoom =
        rooms.find(
            room =>
                room.id === roomId
        );


    if (!selectedRoom) {

        return;
    }


    $("selectedRoomId").value =
        selectedRoom.id;


    $("modalTitle").textContent =
        `Room ${selectedRoom.roomNumber}`;


    $("modalMeta").textContent =

        `${selectedRoom.roomType} • ₹${Number(
            selectedRoom.pricePerNight
        ).toLocaleString()} per night`;


    $("modalIn").value =
        $("checkIn").value;


    $("modalOut").value =
        $("checkOut").value;


    updateTotal();


    $("modal").classList.add(
        "open"
    );
}



// ==========================================
// CLOSE MODAL
// ==========================================

function closeModal() {

    $("modal").classList.remove(
        "open"
    );
}



// ==========================================
// CALCULATE TOTAL
// ==========================================

function updateTotal() {

    if (!selectedRoom) {

        return;
    }


    const checkIn =
        new Date(
            $("modalIn").value
        );


    const checkOut =
        new Date(
            $("modalOut").value
        );


    const nights =
        Math.round(
            (
                checkOut -
                checkIn
            )
            /
            86400000
        );


    if (nights > 0) {

        const total =
            nights *
            selectedRoom.pricePerNight;


        $("total").textContent =

            `${nights} night${nights > 1 ? "s" : ""} × ₹${Number(
                selectedRoom.pricePerNight
            ).toLocaleString()} = ₹${Number(
                total
            ).toLocaleString()}`;

    } else {

        $("total").textContent =
            "Choose valid dates.";
    }
}



// ==========================================
// BOOKING FORM
// ==========================================

async function submitBooking(event) {

    event.preventDefault();


    const bookingData = {

        name:
            $("name").value,

        phone:
            $("phone").value,

        email:
            $("email").value,

        roomId:
            Number(
                $("selectedRoomId").value
            ),

        checkIn:
            $("modalIn").value,

        checkOut:
            $("modalOut").value,

        paymentMethod:
            $("payment").value
    };


    try {

        const response =
            await fetch(
                "/api/bookings",
                {

                    method: "POST",

                    headers: {

                        "Content-Type":
                            "application/json"

                    },

                    body:
                        JSON.stringify(
                            bookingData
                        )
                }
            );


        const data =
            await response.json();


        if (!response.ok) {

            throw new Error(
                data.error
            );
        }


        closeModal();


        $("bookingResult").innerHTML =
            bookingHTML(data);


        $("bookingId").value =
            data.bookingId;


        toast(
            `Booking ${data.bookingId} confirmed!`
        );


        loadStats();

        loadRooms();


        $("manage").scrollIntoView({
            behavior: "smooth"
        });


    } catch (error) {

        toast(
            error.message
        );
    }
}



// ==========================================
// BOOKING HTML
// ==========================================

function bookingHTML(booking) {

    return `

        <div class="result">

            <span>

                BOOKING ID

                <b>
                    ${booking.bookingId}
                </b>

            </span>


            <span>

                GUEST

                <b>
                    ${booking.name}
                </b>

            </span>


            <span>

                ROOM

                <b>
                    ${booking.roomNumber}
                    •
                    ${booking.roomType}
                </b>

            </span>


            <span>

                DATES

                <b>
                    ${booking.checkIn}
                    →
                    ${booking.checkOut}
                </b>

            </span>


            <span>

                NIGHTS

                <b>
                    ${booking.nights}
                </b>

            </span>


            <span>

                TOTAL

                <b>
                    ₹${Number(
                        booking.total
                    ).toLocaleString()}
                </b>

            </span>


            <span>

                PAYMENT

                <b>
                    ${booking.paymentMethod}
                </b>

            </span>


            <span>

                TRANSACTION

                <b>
                    ${booking.transactionId}
                </b>

            </span>

        </div>

    `;
}



// ==========================================
// LOOKUP BOOKING
// ==========================================

async function lookupBooking() {

    const bookingId =
        $("bookingId")
            .value
            .trim();


    if (!bookingId) {

        toast(
            "Enter your booking ID."
        );

        return;
    }


    try {

        const response =
            await fetch(
                `/api/bookings/${encodeURIComponent(
                    bookingId
                )}`
            );


        const data =
            await response.json();


        if (!response.ok) {

            throw new Error(
                data.error
            );
        }


        let html =
            bookingHTML(data);


        if (
            data.status ===
            "CONFIRMED"
        ) {

            html += `

                <button
                    class="cancel"
                    onclick="cancelBooking('${data.bookingId}')">

                    Cancel reservation

                </button>

            `;

        } else {

            html += `

                <p>
                    This reservation is already cancelled.
                </p>

            `;
        }


        $("bookingResult").innerHTML =
            html;


    } catch (error) {

        $("bookingResult").innerHTML =

            `<p>
                ${error.message}
            </p>`;
    }
}



// ==========================================
// CANCEL BOOKING
// ==========================================

async function cancelBooking(
    bookingId
) {

    const confirmed =
        confirm(
            "Are you sure you want to cancel this reservation?"
        );


    if (!confirmed) {

        return;
    }


    try {

        const response =
            await fetch(
                `/api/bookings/${encodeURIComponent(
                    bookingId
                )}/cancel`,
                {
                    method: "PATCH"
                }
            );


        const data =
            await response.json();


        if (!response.ok) {

            throw new Error(
                data.error
            );
        }


        toast(
            "Reservation cancelled successfully."
        );


        lookupBooking();

        loadRooms();

        loadStats();


    } catch (error) {

        toast(
            error.message
        );
    }
}



// ==========================================
// LOAD DASHBOARD STATS
// ==========================================

async function loadStats() {

    try {

        const response =
            await fetch(
                "/api/stats"
            );


        const data =
            await response.json();


        $("roomsCount").textContent =
            data.rooms;


        $("bookingsCount").textContent =
            data.bookings;


        $("customersCount").textContent =
            data.customers;


    } catch (error) {

        console.log(
            "Unable to load statistics."
        );
    }
}



// ==========================================
// DATE INPUT EVENTS
// ==========================================

$("modalIn")
    .addEventListener(
        "change",
        updateTotal
    );


$("modalOut")
    .addEventListener(
        "change",
        updateTotal
    );



// ==========================================
// INITIALIZATION
// ==========================================

$("checkIn").value =
    today();


$("checkOut").value =
    tomorrow();


$("checkIn").min =
    today();


$("checkOut").min =
    tomorrow();


$("modalIn").min =
    today();


$("modalOut").min =
    tomorrow();


loadStats();

loadRooms();



// ==========================================
// REAL-TIME REFRESH
// ==========================================

setInterval(

    function () {

        loadStats();


        if (
            $("checkIn").value &&
            $("checkOut").value
        ) {

            loadRooms();

        }

    },

    5000

);