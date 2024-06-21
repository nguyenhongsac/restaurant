//Cấu hình kích thước phần tử phù hợp với kích thước màn hình
function adjustMainContentHeight() {
	const topBarHeight = document.querySelector('.top-bar').offsetHeight;
	const totalBarHeight = topBarHeight;
	const topBillHeight = document.querySelector('.top-bill').offsetHeight;
	const BotBillHeight = document.querySelector('.bot-bill').offsetHeight;
	const totalBillHeight = topBillHeight + BotBillHeight;

	const mainContent = document.getElementById('mainContent');
	mainContent.style.height = `calc(100vh - ${totalBarHeight}px)`;

	const mainContentBill = document.getElementById('bill');
	mainContentBill.style.height = `calc(100vh - ${totalBarHeight}px - ${totalBillHeight}px - 50px)`;
}

// Gọi hàm khi tải trang
window.onload = adjustMainContentHeight;
// Gọi hàm khi thay đổi kích thước cửa sổ
window.onresize = adjustMainContentHeight;

//Thêm món ăn mới vào danh sách order, nếu đã tồn tại thì tăng số lượng
function addToBill(button) {
	const foodId = button.id;
	const foodName = button.getAttribute('data-name');
	const foodPrice = button.getAttribute('data-price');

	orderItems.push({
		id: i,
		foodId: parseInt(foodId),
		quantity: 1,
		itemName: foodName,
		price: parseFloat(foodPrice),
		note: ''
	});
	i++;
	length++;

	updateBill();
}


//Cập nhật số lượng món ăn từ input
function updateQuantity(id, inputQuantity) {
	orderItems.forEach(item => {
		if (item.id == id) {
			item.quantity = parseInt(inputQuantity);
		}
	})
	//console.log(orderItems)
	updateBill();
}


//Cập nhật danh sách order lên trang
function updateBill() {
	const billDiv = document.getElementById('bill');
	billDiv.innerHTML = '';
	orderItems.forEach(item => {
		if (item.quantity > 0) {
			const orderItemDiv = document.createElement('div');
			orderItemDiv.classList.add('order-item', 'row', 'my-3');

			const quantityInput = document.createElement('input');
			quantityInput.type = 'number';
			quantityInput.value = item.quantity;
			quantityInput.min = '0';
			quantityInput.id = item.id;
			quantityInput.classList.add('quantity', 'col-sm-1', 'p-0', 'ms-3', 'fs-5');
			quantityInput.onchange = () => updateQuantity(quantityInput.id, quantityInput.value);

			const itemName = document.createElement('span');
			itemName.classList.add('col-sm-6', 'ms-3', 'p-0', 'fs-5');
			if (item.note != null && item.note != '') {
				itemName.textContent = item.itemName + '(' + item.note + ')';
			} else {
				itemName.textContent = item.itemName;
			}

			itemName.addEventListener('click', function() {
				const input = document.createElement('input');
				input.type = 'text';
				input.classList.add('col-sm-6', 'ms-3', 'p-0', 'fs-5');
				input.value = item.note;
				input.placeholder = 'note'

				this.replaceWith(input);
				input.focus();

				input.addEventListener('blur', function() {
					item.note = input.value;
					if (item.note != null && item.note != '') {
						itemName.textContent = item.itemName + '(' + item.note + ')';
					} else {
						itemName.textContent = item.itemName;
					}
					input.replaceWith(itemName);
				});

				input.addEventListener('keypress', function(e) {
					if (e.key === 'Enter') {
						item.note = input.value;
						if (item.note != null && item.note != '') {
							itemName.textContent = item.itemName + '(' + item.note + ')';
						} else {
							itemName.textContent = item.itemName;
						}
						input.replaceWith(itemName);
					}
				});
			});
			const priceDiv = document.createElement('div');
			priceDiv.classList.add('col-sm-3', 'float-end', 'ms-3', 'p-0');
			const itemPrice = document.createElement('span');
			itemPrice.classList.add('p-0', 'fs-5');
			itemPrice.textContent = `${item.price}vnđ`;

			priceDiv.appendChild(itemPrice)
			orderItemDiv.appendChild(quantityInput);
			orderItemDiv.appendChild(itemName);
			orderItemDiv.appendChild(priceDiv);

			billDiv.appendChild(orderItemDiv);
		}
		console.log(item.foodId + item.itemName + item.quantity + item.price)
	})
}

//Update cột bill ngay khi load xong trang
document.addEventListener('DOMContentLoaded', function() {
	updateBill();
});


//Xóa toàn bộ order 
function deleteAllBill() {
	const confirmed = confirm("Bạn có chắc chắn muốn xóa không?");
	if (confirmed) {
		orderItems.forEach(item => {
			item.quantiy = 0;
		})

		updateBill;
	}
}

//Lưu order khi bấm nút back quay về home
document.getElementById('back').onclick = function() {
	//console.log(orderItems)
	const tableId = document.getElementById('bill').className;
	// Gửi thông tin đơn hàng về server
	fetch('/restaurant/order/' + tableId + '/save', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(orderItems)
	})
};


//Chuyển order sang bàn khác
function tableClick(table) {
	const newTableId = table.id;

	fetch('/restaurant/order/' + tableId + '/changeTable/' + newTableId, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		}
	})
	const button = document.getElementById('dropdownTableButton');
	button.textContent = table.textContent;
	console.log('Chuyển bàn thành công!');
	setTimeout(function() {
		window.location.href = '/restaurant/order/' + newTableId
	}, 1000)
}


//Lưu danh sách order về server và in phiếu báo bếp
function printOrder() {
	// Gửi thông tin đơn hàng về server
	fetch('/restaurant/order/' + tableId + '/save', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(orderItems)
	})
	console.log(orderItems);
	//Gửi yêu cầu in phiếu
	fetch('/restaurant/order/' + tableId + '/print-order', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		}
	})
		.then(response => response.blob())
		.then(blob => {
			const url = window.URL.createObjectURL(new Blob([blob]));
			const link = document.createElement('a');
			link.href = url;
			link.setAttribute('download', 'order ' + tableId + '.pdf');
			document.body.appendChild(link);
			link.click();
			link.parentNode.removeChild(link);
		})
		.catch(error => console.error('Error:', error));
}

function payment() {
	fetch('/restaurant/order/' + tableId + '/save', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(orderItems)
	})
	setTimeout(function() {
		if (document.querySelector('.order-item') != null) {
			window.location.href = '/restaurant/payment/' + tableId;
		} else {
			window.alert("Không có mặt hàng nào để thanh toán!");
		}
	}, 200)
}