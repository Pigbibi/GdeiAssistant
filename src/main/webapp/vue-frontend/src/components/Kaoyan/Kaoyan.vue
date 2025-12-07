<template>
    <div class="kaoyan">
        <!-- 返回按钮 -->
        <div class="weui-cells__title" @click="goBack">返回</div>

        <div class="hd">
            <h1 class="page_title">考研成绩查询</h1>
        </div>

        <!-- 用户填写信息页面 -->
        <div id="input" v-if="showInput">
            <!-- 提交的查询信息表单 -->
            <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">姓名</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <input id="name" v-model="formData.name" class="weui-input" type="text" maxlength="15" name="name" placeholder="请输入姓名">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">考号</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <input id="examNumber" v-model="formData.examNumber" class="weui-input" type="number" maxlength="15" name="examNumber" pattern="[0-9]*" placeholder="请输入准考证号">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">证件号</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <input id="idNumber" v-model="formData.idNumber" class="weui-input" type="text" maxlength="18" name="idNumber" placeholder="请输入证件号码">
                    </div>
                </div>
            </div>

            <!-- 提交按钮 -->
            <div class="weui-btn-area">
                <a class="weui-btn weui-btn_primary" id="submit" href="javascript:;" @click="postQueryForm">查询</a>
            </div>

            <!-- 备用查询入口 -->
            <div class="weui-panel weui-panel_access">
                <div class="weui-panel__bd">
                    <a href="https://yz.chsi.com.cn/apply/cjcx/" class="weui-media-box weui-media-box_appmsg" target="_blank">
                        <div class="weui-media-box__hd weui-media-box__hd_in-appmsg">
                            <img class="weui-media-box__thumb" src="https://gss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/d01373f082025aaf80011bc5f4edab64034f1a53.jpg" alt="">
                        </div>
                        <div class="weui-media-box__bd weui-media-box__bd_in-appmsg">
                            <h4 class="weui-media-box__title">研究生招生信息网</h4>
                            <p class="weui-media-box__desc">研招网官方考研成绩查询入口</p>
                        </div>
                    </a>
                </div>
            </div>
        </div>

        <!-- 成绩信息显示页面 -->
        <div id="result" v-if="!showInput">
            <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">姓名</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <span>{{resultData.name}}</span>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">考号</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <span>{{resultData.examNumber}}</span>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">总分</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <span>{{resultData.totalScore}}</span>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">科目一</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <span>{{resultData.firstScore}}</span>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">科目二</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <span>{{resultData.secondScore}}</span>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">科目三</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <span>{{resultData.thirdScore}}</span>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">科目四</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <span>{{resultData.fourthScore}}</span>
                    </div>
                </div>
            </div>

            <!-- 按钮区域 -->
            <div class="weui-btn-area">
                <a class="weui-btn weui-btn_default" href="javascript:;" @click="resetQuery">重新查询</a>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: 'Kaoyan',
        data() {
            return {
                formData: {
                    name: '',
                    examNumber: '',
                    idNumber: ''
                },
                resultData: {
                    name: '',
                    examNumber: '',
                    totalScore: '',
                    firstScore: '',
                    secondScore: '',
                    thirdScore: '',
                    fourthScore: ''
                },
                showInput: true
            }
        },
        mounted() {
        },
        methods: {
            // 返回上一页
            goBack() {
                this.$router.go(-1)
            },
            // 查询考研成绩
            postQueryForm() {
                // 表单验证
                if (!this.formData.name) {
                    this.$toast.fail('请输入姓名')
                    return
                }
                if (!this.formData.examNumber) {
                    this.$toast.fail('请输入准考证号')
                    return
                }
                if (!this.formData.idNumber) {
                    this.$toast.fail('请输入证件号码')
                    return
                }

                // 显示加载提示
                this.$toast.loading({
                    message: '查询中...',
                    forbidClick: true,
                    duration: 0
                })

                // 实现考研成绩查询API调用
                axios.post('/rest/kaoyanquery', this.formData)
                    .then(response => {
                        this.$toast.clear()
                        if (response.data.success) {
                            this.resultData = response.data.data
                            this.showInput = false
                        } else {
                            this.$toast.fail(response.data.message || '查询失败')
                        }
                    })
                    .catch(error => {
                        this.$toast.clear()
                        this.$toast.fail('查询失败，请重试')
                        console.error('查询失败:', error)
                    })
            },
            // 重新查询
            resetQuery() {
                this.showInput = true
                this.formData = {
                    name: '',
                    examNumber: '',
                    idNumber: ''
                }
            }
        }
    }
</script>

<style scoped>
    /* 保持原有CSS样式，这里可以添加特定的样式 */
</style>