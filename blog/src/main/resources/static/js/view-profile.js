document.addEventListener('DOMContentLoaded', function() {
    // Load và hiển thị thông tin người dùng
    loadUserData();
    
    // Load avatar
    const savedAvatar = localStorage.getItem('userAvatar');
    if (savedAvatar) {
        document.getElementById('avatarImg').src = savedAvatar;
    }
});

function loadUserData() {
    const savedUserData = localStorage.getItem('userData');
    if (savedUserData) {
        const userData = JSON.parse(savedUserData);
        
        // Cập nhật thông tin hiển thị
        document.getElementById('viewFullName').textContent = userData.fullName;
        document.getElementById('viewEmail').textContent = userData.email;
        document.getElementById('viewPhone').textContent = userData.phone;
        document.getElementById('viewBirthday').textContent = formatDate(userData.birthday);
        document.getElementById('viewBio').textContent = userData.bio;

        // Cập nhật social links
        updateSocialLink('viewFacebook', userData.social.facebook);
        updateSocialLink('viewInstagram', userData.social.instagram);
        updateSocialLink('viewTwitter', userData.social.twitter);
    }
}

function updateSocialLink(elementId, url) {
    const element = document.getElementById(elementId);
    if (url) {
        element.href = url;
        element.style.opacity = '1';
    } else {
        element.style.opacity = '0.5';
        element.href = '#';
    }
}

function formatDate(dateString) {
    if (!dateString) return 'Chưa cập nhật';
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
}

function toggleEditMode() {
    const viewMode = document.getElementById('viewMode');
    const editMode = document.getElementById('editMode');
    
    if (viewMode.classList.contains('hidden')) {
        // Chuyển từ edit mode sang view mode
        viewMode.classList.remove('hidden');
        editMode.classList.add('hidden');
        // Reload data để cập nhật view
        loadUserData();
    } else {
        // Chuyển từ view mode sang edit mode
        viewMode.classList.add('hidden');
        editMode.classList.remove('hidden');
        // Có thể thêm code để load form edit
        window.location.href = 'profile.html';
    }
} 