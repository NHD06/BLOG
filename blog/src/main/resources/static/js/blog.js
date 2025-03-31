// Thêm event listeners cho các filter links
document.querySelectorAll('.filter-item').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        // Remove active class from all links
        document.querySelectorAll('.filter-item').forEach(item => {
            item.classList.remove('active');
        });
        // Add active class to clicked link
        link.classList.add('active');
        // Get category and filter
        const category = link.getAttribute('data-category');
        filterBlogs(category);
    });
});

// Thêm event listener cho search input
document.getElementById('searchInput').addEventListener('input', () => {
    const activeCategory = document.querySelector('.filter-item.active').getAttribute('data-category');
    filterBlogs(activeCategory);
});

function filterBlogs(selectedCategory) {
    const searchQuery = document.getElementById('searchInput').value.toLowerCase();
    const blogCards = document.querySelectorAll('.blog-card');

    blogCards.forEach(card => {
        const category = card.getAttribute('data-category');
        const title = card.querySelector('.blog-title').textContent.toLowerCase();
        
        // Hiển thị card nếu:
        // 1. Category là 'all' hoặc khớp với category được chọn
        // 2. VÀ tiêu đề chứa chuỗi tìm kiếm
        if ((selectedCategory === 'all' || category === selectedCategory) && 
            title.includes(searchQuery)) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

function toggleOptionsMenu(button) {
    const menu = button.nextElementSibling;
    // Đóng tất cả các menu khác
    document.querySelectorAll('.options-menu').forEach(m => {
        if (m !== menu) m.classList.remove('show');
    });
    // Toggle menu hiện tại
    menu.classList.toggle('show');
}

// Đóng menu khi click ra ngoài
document.addEventListener('click', (e) => {
    if (!e.target.closest('.more-options')) {
        document.querySelectorAll('.options-menu').forEach(menu => {
            menu.classList.remove('show');
        });
    }
});
