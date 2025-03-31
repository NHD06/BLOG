// Đóng tất cả các menu khác khi click ra ngoài
document.addEventListener('click', function(event) {
    const menus = document.querySelectorAll('.options-menu');
    menus.forEach(menu => {
        if (!menu.parentElement.contains(event.target)) {
            menu.classList.remove('show');
        }
    });
});

// Toggle menu khi click vào nút
function toggleOptionsMenu(button) {
    event.stopPropagation(); // Ngăn event click lan ra document
    
    // Đóng tất cả các menu khác
    const allMenus = document.querySelectorAll('.options-menu');
    allMenus.forEach(menu => {
        if (menu !== button.nextElementSibling) {
            menu.classList.remove('show');
        }
    });
    
    // Toggle menu hiện tại
    const menu = button.nextElementSibling;
    menu.classList.toggle('show');
} 