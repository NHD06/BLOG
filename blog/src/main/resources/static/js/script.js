// Function to load HTML components
async function loadComponent(elementId, path) {
    try {
        const response = await fetch(path);
        const html = await response.text();
        document.getElementById(elementId).innerHTML = html;
    } catch (error) {
        console.error('Error loading component:', error);
    }
}

// Load components when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    loadComponent('header', '/components/header.html');
    loadComponent('footer', '/components/footer.html');
}); 

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
function toggleOptionsMenu(button, event) {
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