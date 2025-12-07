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
            
            <p class="page_desc" style="margin-top: 25px">点击登录按钮表示你已阅读并同意
                <a class="page_desc" @click="viewUserAgreement">《用户协议》</a>
            </p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'WechatAttach',
    data() {
        return {
            loginForm: {
                username: '',
                password: ''
            },
            loading: false,
            showError: false,
            errorMessage: ''
        };
    },
    methods: {
        // 提交登录表单
        postLoginForm() {
            if (!this.loginForm.username || !this.loginForm.password) {
                this.showErrorMessage('请将信息填写完整！');
                return;
            }
            
            this.loading = true;
            
            axios.post('/wechat/userattach', {
                username: this.loginForm.username,
                password: this.loginForm.password
            })
            .then(response => {
                this.loading = false;
                if (response.data.success) {
                    this.$weui.alert('绑定校园网络账号成功', {
                        title: '绑定成功',
                        buttons: [{
                            label: '进入功能主页',
                            type: 'primary',
                            onClick: () => {
                                window.location.href = '/index';
                            }
                        }]
                    });
                } else {
                    this.showErrorMessage(response.data.message);
                }
            })
            .catch(error => {
                this.loading = false;
                if (error.response) {
                    this.showErrorMessage(error.response.data.message);
                } else {
                    this.showErrorMessage('网络连接失败，请检查网络连接！');
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
        
        // 查看用户协议
        viewUserAgreement() {
            window.location.href = '/agreement';
        }
    }
};
</script>

<style scoped>
@import '../../../static/css/common/common.css';
@import '../../../static/css/common/weui.min.css';
</style>