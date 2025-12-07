<template>
    <div class="evaluate">
        <!-- 返回按钮 -->
        <div class="header">
            <div class="back" @click="$router.back()">
                <i class="icon-back"></i>
                <span>返回</span>
            </div>
            <div class="title">教学质量评价</div>
        </div>

        <!-- 直接提交教学评价信息选项 -->
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell weui-cell_switch">
                <div class="weui-cell__bd">直接提交教学评价信息</div>
                <div class="weui-cell__ft">
                    <input id="directlySubmit" class="weui-switch" type="checkbox" v-model="directlySubmit">
                </div>
            </div>
        </div>

        <!-- 提交信息按钮 -->
        <div class="weui-btn_area">
            <a class="weui-btn weui-btn_primary" href="javascript:" @click="postEvaluateForm">提交信息</a>
        </div>

        <p class="page_desc" style="margin-top: 25px">注意：教学评价信息提交后，将不能再作修改</p>
    </div>
</template>

<script>
export default {
    name: 'Evaluate',
    data() {
        return {
            directlySubmit: false // 是否直接提交教学评价信息
        }
    },
    mounted() {
    },
    methods: {
        // 提交教学评价表单
        postEvaluateForm() {
            // 显示加载提示
            this.$toast.loading({
                message: '提交中...',
                forbidClick: true
            });
            
            // 实现教学评价提交API调用
            this.$axios.post('/api/evaluate', {
                directlySubmit: this.directlySubmit
            })
            .then(response => {
                this.$toast.clear();
                if (response.data.success) {
                    if (this.directlySubmit) {
                        this.$alert('已提交教学质量评价信息', '教学质量评价成功', {
                            confirmButtonText: '好',
                            type: 'success'
                        });
                    } else {
                        this.$alert('请登录教务系统进行最终确认', '教学质量评价成功', {
                            confirmButtonText: '好',
                            type: 'success'
                        });
                    }
                } else {
                    this.$toast.fail(response.data.message || '提交失败');
                }
            })
            .catch(error => {
                this.$toast.clear();
                console.error('Error submitting evaluate:', error);
                this.$toast.fail('网络请求失败，请检查网络连接');
            });
        }
    }
}
</script>

<style>
    /* 引入样式文件 */
    @import '../../static/css/common/weui.min.css';
    @import '../../static/css/common/common.css';
    
    /* 基本样式 */
    .evaluate {
        padding: 15px;
    }
    
    /* 头部样式 */
    .header {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
    }
    
    .back {
        display: flex;
        align-items: center;
        color: #333;
    }
    
    .icon-back {
        width: 20px;
        height: 20px;
        margin-right: 5px;
        background: url('/img/common/back.png') no-repeat center center;
        background-size: contain;
    }
    
    .title {
        flex: 1;
        text-align: center;
        font-weight: bold;
        font-size: 16px;
    }
</style>