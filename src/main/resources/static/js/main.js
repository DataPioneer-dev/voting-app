let list = document.querySelectorAll(".navigation li");

function activeLink() {
    list.forEach((item) => {
        item.classList.remove("hovered");
    });
    this.classList.add("hovered");
}

list.forEach((item) => item.addEventListener("mouseover", activeLink));

// Menu Toggle
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
    navigation.classList.toggle("active");
    main.classList.toggle("active");
};

function validatePasswords(event) {
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;
    const errorMessage = document.getElementById("error-message");

    if (password !== confirmPassword) {
        event.preventDefault();
        errorMessage.innerHTML = `
                    <div class="warning-box">
                        <ion-icon name="alert-circle-outline"></ion-icon>
                        <span>Passwords do not match. Please try again.</span>
                    </div>
                `;
        return false;
    }

    errorMessage.innerHTML = "";
    return true;
}

function addOption() {
    const optionsContainer = document.getElementById('options-container');
    const optionCount = optionsContainer.children.length;

    const newOption = document.createElement('input');
    newOption.type = 'text';
    newOption.name = `options[${optionCount}]`;
    newOption.placeholder = `Option ${optionCount + 1}`;
    newOption.required = true;

    optionsContainer.appendChild(newOption);
}

function preparePollResultsChartData(polls) {
    const pollData = {
        labels: [""],
        datasets: [],
    };

    polls.forEach((poll, index) => {
        const totalVotesForPoll = poll.optionList.reduce((sum, option) => sum + option.votes, 0);
        const backgroundColor = generateColorPalette(1)[0];

        pollData.datasets.push({
            label: poll.question,
            data: [totalVotesForPoll],
            backgroundColor: backgroundColor,
            borderColor: "rgba(75, 192, 192, 1)",
            borderWidth: 1,
        });
    });

    return pollData;
}

function generateColorPalette(count) {
    const colors = [];
    for (let i = 0; i < count; i++) {
        const randomColor = `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 0.6)`;
        colors.push(randomColor);
    }
    return colors;
}

document.addEventListener("DOMContentLoaded", function () {

    const warningBox = document.querySelector(".warning-box");
    const successBox = document.querySelector(".success-box");

    if (successBox) {
        setTimeout(() => {
            window.location.href = "polls";
        }, 2000);
    }

    if (warningBox) {
        setTimeout(() => {
            warningBox.style.transition = "opacity 1s ease";
            warningBox.style.opacity = 0;
            setTimeout(() => warningBox.style.display = "none", 1000);
        }, 4000);
    }

    const pollsJson = document.getElementById("pollsJson")?.getAttribute("data-json");
    const polls = JSON.parse(pollsJson || "[]");

    if (polls.length === 0) {
        console.warn("No poll data available.");
        return;
    }

    const pollResultsChartData = preparePollResultsChartData(polls);
    createResponsiveBarChart("pollResultsChart", pollResultsChartData);
});

// Responsive bar chart
function createResponsiveBarChart(canvasId, chartData) {
    const ctx = document.getElementById(canvasId).getContext("2d");
    new Chart(ctx, {
        type: "bar",
        data: chartData,
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    display: true,
                    position: "top",
                },
                tooltip: {
                    enabled: true,
                    callbacks: {
                        label: (context) => `${context.dataset.label}: ${context.raw} votes`,
                    },
                },
            },
            scales: {
                x: {
                    ticks: {
                        autoSkip: false,
                    },
                    title: {
                        display: true,
                        text: "Polls",
                    },
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: "Total Votes",
                    },
                },
            },
        },
    });
}