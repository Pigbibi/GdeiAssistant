<template>
    <div class="login">
        <div class="hd">
            <h1 class="page_title">广东二师助手</h1>
            <p class="page_desc">请登录校园网系统</p>
        </div>

        <!-- 提交的用户信息表单 -->
        <div class="weui-cells weui-cells_form">
            <form>
                <input type="hidden" id="redirect" name="redirect" :value="redirectUrl">
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">账号</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <input id="username" v-model="formData.username" class="weui-input" type="text" maxlength="20" name="username"
                               placeholder="请输入你的校园网账号">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">密码</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <input id="password" v-model="formData.password" class="weui-input" type="password" maxlength="35" name="password"
                               placeholder="请输入你的校园网密码">
                    </div>
                </div>
            </form>
        </div>

        <!-- 登录按钮 -->
        <div class="weui-btn_area">
            <a class="weui-btn weui-btn_primary" href="javascript:" @click="postLoginForm">登录</a>
        </div>

        <!-- 提示信息 -->
        <p class="page_desc" style="margin-top: 25px">关于登录账户请阅读
            <a class="page_desc"
               @click="window.location.href = '/about/account'">《校园网络账号说明》
            </a>
            <br>
            使用前请仔细阅读
            <a @click="window.location.href = '/agreement'">《用户协议》</a>和<a
                    @click="window.location.href = '/policy/privacy'">《隐私政策》</a>
        </p>

        <!-- 快速登录 -->
        <div class="quick-login">
            <div class="quick-login-text">
                <p>——&nbsp;&nbsp;&nbsp;其他方式登录&nbsp;&nbsp;&nbsp;——</p>
            </div>
            <div class="quick-login-button">
                <img src="/img/login/qq.png" width="30" height="30"/>
                <img src="/img/login/wechat.png" width="30" height="30"/>
                <img src="/img/login/weibo.png" width="30" height="30"/>
                <img src="/img/login/apple.png" width="30" height="30"/>
            </div>
        </div>

        <p class="page_desc"></p>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: 'Login',
        props: {
            redirectUrl: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                formData: {
                    username: '',
                    password: ''
                }
            }
        },
        mounted() {
        },
        methods: {
            //提交表单数据登录
            postLoginForm() {
                if (this.formData.username === '' || this.formData.password === '') {
                    this.$toast.fail('请将信息填写完整')
                    return
                }
                //弹出用户协议和隐私政策提示
                this.$dialog.confirm({
                    title: '用户协议和隐私政策提示',
                    message: '在您使用广东二师助手前，请您认真阅读并了解《用户协议》和《隐私政策》。如您是未满18周岁的未成年人，你还需通知您的监护人共同阅读用户协议和隐私政策，点击"确定"即表示您已阅读并同意全部条款。若您不同意，请点击"取消"并退出应用。若您未注册广东二师助手账号，点击登录时系统将自动为您创建一个广东二师助手账号。'
                }).then(() => {
                    // 显示加载提示
                    this.$toast.loading({
                        message: '登录中...',
                        forbidClick: true,
                        duration: 0
                    })

                    // 实现登录API调用
                    axios.post('/api/userlogin', this.formData)
                        .then(response => {
                            this.$toast.clear()
                            if (response.data.success) {
                                if (this.redirectUrl !== '') {
                                    window.location.href = this.redirectUrl
                                } else {
                                    window.location.href = '/index'
                                }
                            } else {
                                this.$toast.fail(response.data.message || '登录失败')
                            }
                        })
                        .catch(error => {
                            this.$toast.clear()
                            if (error.response) {
                                this.$toast.fail(error.response.data.message || '登录失败')
                            } else {
                                this.$toast.fail('网络访问异常，请检查网络连接')
                            }
                            console.error('登录失败:', error)
                        })
                }).catch(() => {
                    // 用户取消操作
                })
            }
        }
    }
</script>

<style scoped>
    /* 保持原有CSS样式，这里可以添加特定的样式 */
</style>