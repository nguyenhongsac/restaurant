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

function showAlert(message) {
  var alertElement = document.getElementById('noticeAlert');
  var alertContent = document.getElementById('alertContent');

  alertElement.classList.remove('fade');
  alertElement.classList.add('show');
  alertContent.innerHTML = message;

  // Automatically hide the alert after .. seconds
  setTimeout(function() {
    alertElement.classList.remove('show');
    alertElement.classList.add('fade');
  }, 2000);
}

// === When available, call comfirmReserve, then call reserveTable ===

const reserve = document.querySelector(".reserve");
let confirmReserveModal;
let cancelReserveModal;
let form;
function confirmReserve(id, status) { // status = true mean table available, call confirm
	if (status) {
		confirmReserveModal = new bootstrap.Modal('#tableReserve'+id, {
			keyboard: false
		});
		confirmReserveModal.show();
	} else {
		cancelReserveModal = new bootstrap.Modal('#tableReserveC'+id, {
    		keyboard: false
  		});
  		cancelReserveModal.show();
	}
	form = document.getElementById('reserveForm'+id);
	form.classList.remove('was-validated');
}
async function reserveTable(id, status) {
	/* active ? 'resever' : 'available', status(true or false) ?'available':'resever'
	Nếu đặt bàn, thêm active và đổi bookmark và ngược lại
	Chỉ thực hiện sau khi post thành công
	*/
	let reserveIcon = document.getElementById("reserveIcon"+id);
	if (status) { // Tạo post request đặt bàn
	    if (form.checkValidity()) {
			form.classList.add('was-validated');
			
			const formData = new FormData(form);
			
			const data = {};
			data['tableId'] = id;
		    formData.forEach((value, key) => {
		      data[key] = value;
		    });
		    
	        await fetch('/restaurant/reserve/admin', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify(data)
	        }).then(response => response.text())
				.then(result => {
					setTimeout(() => {
			            location.reload();
			        }, 2000);
					showAlert(result);
				})
				.catch((error) => {
					console.error('Error:', error);
					setTimeout(() => {
			            location.reload();
			        }, 2000);
					showAlert('Reserve table failed!');
				});

			reserveIcon.classList.remove('bi-bookmark');
	    	reserveIcon.classList.add('bi-bookmark-check-fill');
	    	reserve.classList.add('active');
	    	
	    	confirmReserveModal.hide();
    	} else {
			form.classList.add('was-validated');
		}
    	
	} else {	// Tạo post request hủy bàn
		await fetch('/restaurant/reserve/cancel?id='+id, {
		   	method: 'POST'
		}).then(response => {
			if (response.ok) {
				setTimeout(() => {
		            location.reload();
		        }, 2000);
				showAlert('Cancel reserving table successfully!');
	    	} else {
	        	showAlert('Cancel reserving table failed!');
	    	}
		});
		
		reserveIcon.classList.add('bi-bookmark');
    	reserveIcon.classList.remove('bi-bookmark-check-fill');
    	reserve.classList.remove('active');
	}
};