"use strict";

(function() {
  /**
   * Animation on scroll function and init
   */
  function aos_init() {
    AOS.init({
      duration: 800,
      easing: 'slide',
      once: true,
      mirror: false
    });
  }
  window.addEventListener('load', () => {
    aos_init();
  });
})();

// Change active view-button
const viewBtn = document.querySelectorAll('.view-button');
const content = document.querySelectorAll('.tab-pane');
const tables = document.querySelectorAll('.set');
let billStatus = false;

const billToggleButton = document.getElementById("custom-toggle-button");

viewBtn.forEach(button => {
  button.addEventListener("click", function() {
    viewBtn.forEach(btn => btn.classList.remove('active'));
    this.classList.add('active');
    document.getElementById("view-bar").classList.remove('view-bar-show');

    // Change lobby content
    content.forEach(content => content.classList.add('d-none'));
    tables.forEach(table => table.classList.remove('d-none'));
    const targetId = this.getAttribute('data-bs-target');
    
    if (targetId != '#bill') {
      // set status bill to false
      billStatus = false;
      billToggleButton.classList.remove('active');

      const targetContent = document.getElementById(targetId.substring(1));
      if (targetContent) {
        targetContent.classList.remove('d-none');
      }
    } else { // show all bill table in restaurant
      
      billStatus = true;
      billToggleButton.classList.add('active');

      content.forEach(content => content.classList.remove('d-none'));
      tables.forEach(table => table.classList.add('d-none'));
      let billTable = document.querySelectorAll('.set-bill');
      billTable.forEach(table => table.classList.remove('d-none'));
    }
  })
});

billToggleButton.addEventListener("click", function() {
  this.classList.toggle("active");

  // Check bill status before filter
  if (!billStatus) {
    if (this.classList.contains('active')) {
      tables.forEach(table => table.classList.add('d-none'));
      let billTable = document.querySelectorAll('.set-bill');
      billTable.forEach(table => table.classList.remove('d-none'));
    } else {
      tables.forEach(table => table.classList.remove('d-none'));
    }
  }
});

// Link navigation
const firstlink = document.getElementById('1stlink');
const secondlink = document.getElementById('2ndlink');

firstlink.addEventListener('click', function(event) {

  event.preventDefault();

  // Simulate a click event on a button
  const button = document.getElementById('1stbutton');
  button.click();
});

secondlink.addEventListener('click', function(event) {

  event.preventDefault();

  // Simulate a click event on a button
  const button = document.getElementById('2ndbutton');
  button.click();
});
