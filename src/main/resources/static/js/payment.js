
document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('bankTransfer').addEventListener('change', function() {
		if (this.checked) {
			document.getElementById('qrCode').style.display = 'block';
			document.getElementById('cashPaymentDetails').style.display = 'none';
		}
	});

	document.getElementById('cashPayment').addEventListener('change', function() {
		if (this.checked) {
			document.getElementById('qrCode').style.display = 'none';
			document.getElementById('cashPaymentDetails').style.display = 'block';
		}
	});

	document.getElementById('amountGiven').addEventListener('input', function() {
		const totalAmount = parseFloat(document.getElementById('totalPrice').textContent.replace(' VND', '').replace(',', ''));
		const amountGiven = parseFloat(this.value);
		const changeDue = amountGiven - totalAmount;
		document.getElementById('changeDue').textContent = changeDue > 0 ? changeDue + ' VND' : '0 VND';
	});
});

// Function to save the blob to cache
async function saveBlobToCache(cacheName, request, blob) {
	const cache = await caches.open(cacheName);
	const response = new Response(blob, { headers: { 'Content-Type': 'application/pdf' } });
	await cache.put(request, response);
}

// Function to download blob from cache
async function downloadBlobFromCache(cacheName, request) {
	const cache = await caches.open(cacheName);
	const response = await cache.match(request);
	if (response) {
		const blob = await response.blob();
		const url = window.URL.createObjectURL(blob);
		const link = document.createElement('a');
		link.href = url;
		link.setAttribute('download', 'Bill ' + tableId + '.pdf');
		document.body.appendChild(link);
		link.click();
		link.parentNode.removeChild(link);
		return true;
	}
	return false;
}

async function submitPayment() {
	const paymentItem = document.querySelector('input[name="paymentMethod"]:checked');
	var pay = false;
	if (paymentItem != null) {
		paymentMethod = paymentItem.value;
		if (paymentMethod === 'cash') {
			const given = document.getElementById('amountGiven').value;
			if (given) {
				const amountGiven = parseFloat(document.getElementById('amountGiven').value);
				const totalAmount = parseFloat(document.getElementById('totalPrice').textContent.replace(' VND', '').replace(',', ''));
				if (amountGiven >= totalAmount) {
					alert(`Thanh toán thành công. Số tiền trả lại: ${amountGiven - totalAmount} VND`);
					pay = true;
				} else {
					alert('Số tiền không đủ để thanh toán!')
				}
			}else{
				pay = true;
			}

		} else if (paymentMethod === 'bank') {
			alert('Bạn đã chọn thanh toán bằng chuyển khoản ngân hàng');
			pay = true;
		}
	} else {
		alert('Vui lòng chọn phương thức thanh toán');
	}

	if (pay) {
		fetch('/restaurant/payment/' + tableId + '/print', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			}
		})
<<<<<<< Updated upstream
		.catch(error => console.error('Error:', error));
=======
			.then(response => response.blob())
			.then(blob => {
				const url = window.URL.createObjectURL(new Blob([blob]));
				const link = document.createElement('a');
				link.href = url;
				link.setAttribute('download', 'Bill ' + tableId + '.pdf');
				document.body.appendChild(link);
				link.click();
				link.parentNode.removeChild(link);
				setTimeout(function() {
					fetch('/restaurant/payment/' + tableId + '/success', {
						method: 'POST',
						headers: {
							'Content-Type': 'application/json'
						}
					})
				}, 2000)
			})
			.catch(error => console.error('Error:', error));
	}
>>>>>>> Stashed changes
}


document.addEventListener('DOMContentLoaded', function() {
	// Lấy phần tử cha để chèn các phần tử mới vào
	const orderDetailsContainer = document.getElementById('order-details');

	// Lặp qua mỗi item trong OrderDetails và tạo phần tử HTML tương ứng
	Object.values(aggregatedOrderDetails).forEach(item => {
		// Tạo div cha cho mỗi item
		const itemDiv = document.createElement('div');
		itemDiv.className = 'row my-3';
		itemDiv.id = item.foodId;

		// Tạo div cho tên món ăn
		const itemNameDiv = document.createElement('div');
		itemNameDiv.className = 'offset-sm-1 col-sm-6 p-0 fs-5';
		itemNameDiv.textContent = item.itemName;

		// Tạo div cho số lượng
		const quantityDiv = document.createElement('div');
		quantityDiv.className = 'col-sm-1 p-0 fs-5 text-center';
		quantityDiv.textContent = item.quantity;

		// Tạo div cho giá
		const priceDiv = document.createElement('div');
		priceDiv.className = 'col-sm-3 p-0 fs-5 text-end';
		priceDiv.textContent = item.price + 'vnđ';

		// Chèn các div con vào div cha
		itemDiv.appendChild(itemNameDiv);
		itemDiv.appendChild(quantityDiv);
		itemDiv.appendChild(priceDiv);

		// Chèn div cha vào container
		orderDetailsContainer.appendChild(itemDiv);

		totalPrice += item.price * item.quantity;
	});

	// Hien thi tien
	document.getElementById('totalPrice').textContent = totalPrice + ' VND';
});