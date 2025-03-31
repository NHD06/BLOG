document.addEventListener('DOMContentLoaded', function() {
    // Xử lý upload avatar
    const avatarUpload = document.getElementById('avatarUpload');
    const avatarImg = document.getElementById('avatarImg');

    avatarUpload.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                avatarImg.src = e.target.result;
                // Lưu vào localStorage
                localStorage.setItem('userAvatar', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    });

    // Load avatar từ localStorage nếu có
    const savedAvatar = localStorage.getItem('userAvatar');
    if (savedAvatar) {
        avatarImg.src = savedAvatar;
    }

    // Xử lý form thông tin cá nhân
    const profileForm = document.getElementById('profileForm');
    profileForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Lấy giá trị từ form
        const userData = {
            fullName: document.getElementById('fullName').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            birthday: document.getElementById('birthday').value,
            bio: document.getElementById('bio').value,
            social: {
                facebook: document.querySelector('input[placeholder="Facebook URL"]').value,
                instagram: document.querySelector('input[placeholder="Instagram URL"]').value,
                twitter: document.querySelector('input[placeholder="Twitter URL"]').value
            }
        };

        // Lưu vào localStorage
        localStorage.setItem('userData', JSON.stringify(userData));
        
        // Hiển thị thông báo
        alert('Đã lưu thông tin thành công!');
    });

    // Load user data từ localStorage nếu có
    const savedUserData = localStorage.getItem('userData');
    if (savedUserData) {
        const userData = JSON.parse(savedUserData);
        document.getElementById('fullName').value = userData.fullName;
        document.getElementById('email').value = userData.email;
        document.getElementById('phone').value = userData.phone;
        document.getElementById('birthday').value = userData.birthday;
        document.getElementById('bio').value = userData.bio;
        // Load social links
        document.querySelector('input[placeholder="Facebook URL"]').value = userData.social.facebook;
        document.querySelector('input[placeholder="Instagram URL"]').value = userData.social.instagram;
        document.querySelector('input[placeholder="Twitter URL"]').value = userData.social.twitter;
    }

    // Xử lý đổi mật khẩu
    const passwordForm = document.getElementById('passwordForm');
    passwordForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const currentPassword = document.getElementById('currentPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        // Validate
        if (newPassword !== confirmPassword) {
            alert('Mật khẩu mới không khớp!');
            return;
        }

        // Kiểm tra mật khẩu hiện tại (demo)
        if (currentPassword === 'demo123') {
            // Lưu mật khẩu mới
            localStorage.setItem('userPassword', newPassword);
            alert('Đổi mật khẩu thành công!');
            passwordForm.reset();
        } else {
            alert('Mật khẩu hiện tại không đúng!');
        }
    });
}); 