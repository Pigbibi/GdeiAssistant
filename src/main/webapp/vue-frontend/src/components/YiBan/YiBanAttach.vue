<template>
    <div class="weui-tab">
        <div class="weui-tab__panel">
            <div class="hd">
                <h1 class="page_title">绑定教务系统</h1>
                <p class="page_desc">请登录教务系统</p>
            </div>
            
            <!-- 提交的用户信息表单 -->
            <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">账号</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <input v-model="loginForm.username" id="username" class="weui-input" type="text" maxlength="20" name="username" placeholder="请输入你的校园网账号">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">密码</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <input v-model="loginForm.password" id="password" class="weui-input" type="password" maxlength="35" name="password" placeholder="请输入你的校园网密码">
                    </div>
                </div>
            </div>
            
            <!-- 登录按钮 -->
            <div class="weui-btn_area">
                <a class="weui-btn weui-btn_primary" href="javascript:" @click="postLoginForm">登录</a>
            </div>
            
            <!-- 登录中弹框 -->
            <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
                <div class="weui-mask_transparent"></div>
                <div class="weui-toast">
                        <span class="weui-primary-loading weui-icon_toast">
                          <span class="weui-primary-loading__dot"></span>
                        </span>
                    <p class="weui-toast__content">登录中</p>
                </div>
            </div>
            
            <!-- 错误提示 -->
            <div class="weui-toptips weui_warn js_tooltips" v-if="showError" v-text="errorMessage"></div>
            
            <p class="page_desc" style="margin-top: 25px">关于登录账户请阅读
                <a class="page_desc" @click="viewAccountInfo">《校园网络账号说明》</a>
            </p>
            
            <p class="page_desc">点击登录按钮表示你已阅读并同意
                <br>
                <a class="page_desc" @click="viewUserAgreement">《用户协议》</a>和
                <a class="page_desc" @click="viewPrivacyPolicy">《隐私政策》</a>
            </p>
            
            <div class="weui-msg">
                <div class="weui_extra_area">
                    <p>关注微信公众号【广东二师助手】</p>
                    <p>获取更多资讯，下载广二师助手APP客户端</p>
                </div>
            </div>
            
            <div id="rnw_cookies_banner" v-if="showCookieBanner" style="display: block;">
                <a class="spritefont-close" id="rnw_cookies_banner_button_close" title="关闭" @click="closeCookieBanner" href="javascript:">×</a>
                <div id="rnw_cookies_banner_text">
                    本网站使用Cookie的目的是为您提供更加简捷和个性化的上网体验。Cookie将有用的信息存储在您的电脑上，可帮助我们改善您浏览我们网站的效率以及对您的实用性。在某些情况下，它们是网站正常运行所必不可少的。如果您访问本网站，即表示您同意我们使用Cookie。
                    请点击<a href="/policy/cookie" id="rnw_cookies_banner_button_more">Cookie政策</a>了解更多信息。
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'YiBanAttach',
    data() {
        return {
            loginForm: {
                username: '',
                password: ''
            },
            loading: false,
            showError: false,
            errorMessage: '',
            showCookieBanner: false
        };
    },
    mounted() {
        // 检查Cookie政策提示
        if (!localStorage.getItem('cookiePolicy')) {
            this.showCookieBanner = true;
        }
    },
    methods: {
        // 提交登录表单
        postLoginForm() {
            if (!this.loginForm.username || !this.loginForm.password) {
                this.showErrorMessage('请将信息填写完整！');
                return;
            }
            
            this.loading = true;
            
            axios.post('/yiban/userattach', {
                username: this.loginForm.username,
                password: this.loginForm.password
            })
            .then(response => {
                this.loading = false;
                if (response.data.success) {
                    // 绑定成功，跳转到首页或指定页面
                    window.location.href = '/index';
                } else {
                    this.showErrorMessage(response.data.message);
                }
            })
            .catch(error => {
                this.loading = false;
                if (error.response) {
                    this.showErrorMessage(error.response.data.message);
                } else {
                    this.showErrorMessage('绑定校园网络账号失败,请检查网络连接！');
                }
            });
        },
        
        // 显示错误信息
        showErrorMessage(message) {
            this.errorMessage = message;
            this.showError = true;
            setTimeout(() => {
                this.showError = false;
            }, 2000);
        },
        
        // 查看校园网络账号说明
        viewAccountInfo() {
            window.location.href = '/about/account';
        },
        
        // 查看用户协议
        viewUserAgreement() {
            window.location.href = '/agreement';
        },
        
        // 查看隐私政策
        viewPrivacyPolicy() {
            window.location.href = '/policy/privacy';
        },
        
        // 关闭Cookie政策提示
        closeCookieBanner() {
            this.showCookieBanner = false;
            localStorage.setItem('cookiePolicy', 1);
        }
    }
};
</script>

<style scoped>
@import '../../../static/css/common/common.css';
@import '../../../static/css/common/weui.min.css';
</style>