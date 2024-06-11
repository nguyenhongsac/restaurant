document.getElementById("custom-toggle-button").addEventListener("click", function() {
    this.classList.toggle("active");
  });

// Change active view-button
const viewBtn = document.querySelectorAll('.view-button');

viewBtn.forEach(button => {
  button.addEventListener("click", function() {
    viewBtn.forEach(btn => btn.classList.remove('active'));
    this.classList.add('active');
    document.getElementById("view-bar").classList.remove('view-bar-show');
  })
})