<template>
    <div class="body1">
        <form id="form">
            <section class="head">
                <p class="opt"><a href="#" class="submit" @click="submitForm">完成</a></p>
                <a href="javascript:history.go(-1);" class="back" @click="goBack"><i class="i iarr"></i></a>
                <span class="t2">发布失物招领信息</span>
            </section>

            <section class="picture">
                <div class="images">
                    <span class="addimg">
                        <i class="i iadd"><i class="i i1"></i><i class="i i2"></i></i>
                        <input type="file" accept="image/*" id="file_input" name="file[]" @change="handleFileSelect">
                    </span>
                    <div v-for="(image, index) in images" :key="index" class="image">
                        <a href="#"><i :id="index" class="i iclose" @click="removeImage(index)"></i></a>
                        <i class="img">
                            <img :src="image" @load="checkimg">
                        </i>
                        <input name="imgs[]" class="upimg" hidden>
                    </div>
                </div>
                <p class="tip">最多可上传4张图片</p>
            </section>

            <section class="form">
                <div class="frm">
                    <p class="frmt">寻找类型</p>
                    <div class="which">
                        <label>
                            <input type="radio" name="lostType" value="0" v-model="formData.lostType" checked>寻物
                        </label>
                        <label>
                            <input type="radio" name="lostType" value="1" v-model="formData.lostType">寻主
                        </label>
                    </div>
                </div>
                <div class="frm">
                    <p class="frmt">物品名称</p>
                    <div class="frmc">
                        <input id="name" type="text" placeholder="最多25个字" v-model="formData.name" name="name" maxlength="25">
                    </div>
                </div>
                <div class="frm">
                    <p class="frmt">物品描述</p>
                    <div class="frmc">
                        <input id="description" type="text" name="description" v-model="formData.description" maxlength="100">
                    </div>
                </div>
                <div class="frm">
                    <p class="frmt place">{{ formData.lostType === '0' ? '丢失地点' : '捡到地点' }}</p>
                    <div class="frmc">
                        <input id="location" type="text" name="location" v-model="formData.location" maxlength="30">
                    </div>
                </div>
                <div class="frm">
                    <p class="frmt select">选择分类</p>
                    <div class="frmc"><b id="selectType" @click="showTypeModal"><span class="selectvalue">{{ selectedType }}</span><i class="i iarrow"></i></b></div>
                    <input id="itemType" type="hidden" name="itemType" v-model="formData.itemType">
                </div>

                <!-- 联系方式 -->
                <div class="contact_tip">QQ号/微信/手机号任填其中一项即可</div>
                <div class="frm">
                    <p class="frmt">QQ号</p>
                    <div class="frmc">
                        <input id="qq" type="text" name="qq" v-model="formData.qq" maxlength="20">
                    </div>
                </div>
                <div class="frm">
                    <p class="frmt">微信号</p>
                    <div class="frmc">
                        <input id="wechat" type="text" name="wechat" v-model="formData.wechat" maxlength="20">
                    </div>
                </div>
                <div class="frm">
                    <p class="frmt">手机号</p>
                    <div class="frmc">
                        <input id="phone" type="text" name="phone" v-model="formData.phone" maxlength="11">
                    </div>
                </div>

                <br><br>
            </section>
        </form>

        <!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
        <div class="weui-toptips weui_warn js_tooltips" v-if="errorMessage">{{ errorMessage }}</div>

        <!-- 分类选择弹窗 -->
        <div class="sky" v-if="showTypeModal">
            <div class="mark"></div>
            <div class="mw typemw">
                <div class="mwt">
                    <a href="javascript:" class="mwclose" @click="hideTypeModal"><i class="i imwclose"></i></a>
                    <p>选择分类</p>
                </div>

                <div class="mwc">
                    <ul>
                        <li v-for="(type, index) in itemTypes" :key="index"><a href="javascript:" @click="selectItemType(type, index)">{{ type }}</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

export default {
    name: 'LostAndFoundPublish',
    data() {
        return {
            formData: {
                lostType: '0',
                name: '',
                description: '',
                location: '',
                itemType: '',
                qq: '',
                wechat: '',
                phone: ''
            },
            itemTypes: [
                '手机', '校园卡', '身份证', '银行卡', '书', '钥匙',
                '包包', '衣帽', '校园代步', '运动健身', '数码配件', '其他'
            ],
            selectedType: '',
            showTypeModal: false,
            errorMessage: '',
            loading: false,
            // 图片相关
            images: [], // 存放图片的Base64编码
            imagesCount: 0, // 当前图片数量
            allowTypes: ['image/jpg', 'image/jpeg', 'image/png', 'image/gif'], // 允许的图片类型
            maxSize: 1024 * 1024 * 5, // 图片最大大小5MB
            maxWidth: 720 // 图片最大宽度
        }
    },
    mounted() {
        // 屏幕适配
        this.screenAdaptation();
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        },
        
        // 调整图片尺寸
        checkimg(e) {
            const obj = e.target;
            if (obj.width > obj.height) {
                obj.style.width = 'auto';
                obj.style.height = '100%';
            }
        },
        
        // 屏幕适配
        screenAdaptation() {
            if (window.innerWidth < 375) {
                document.querySelector('.picture .images').classList.add('forp4');
            }
            
            const ua = window.navigator.userAgent;
            if (!(/\(i[^;]+;( U;)? CPU.+Mac OS X/).test(ua) && ua.indexOf('Android') === -1 && ua.indexOf('Linux') === -1) {
                document.body.classList.add('isPC');
            }
        },
        
        // 显示错误提示
        showErrorTip(message) {
            this.errorMessage = message;
            setTimeout(() => {
                this.errorMessage = '';
            }, 2000);
        },
        
        // 显示分类选择弹窗
        showTypeModal() {
            this.showTypeModal = true;
            document.documentElement.style.overflow = 'hidden';
        },
        
        // 隐藏分类选择弹窗
        hideTypeModal() {
            this.showTypeModal = false;
            document.documentElement.style.overflow = 'auto';
        },
        
        // 选择分类
        selectItemType(type, index) {
            this.selectedType = type;
            this.formData.itemType = index.toString();
            this.hideTypeModal();
        },
        
        // 处理文件选择
        handleFileSelect(event) {
            const files = event.target.files;
            
            if (files.length === 0) {
                return;
            }
            
            const file = files[0];
            
            // 检查文件类型
            if (this.allowTypes.indexOf(file.type) === -1) {
                this.showErrorTip('不合法的图片文件类型');
                return;
            }
            
            // 检查文件大小
            if (file.size > this.maxSize) {
                this.showErrorTip('图片文件不能超过5MB');
                return;
            }
            
            // 检查图片数量
            if (this.images.length >= 4) {
                this.showErrorTip('最多只能选择四张图片！');
                return;
            }
            
            const reader = new FileReader();
            
            reader.readAsDataURL(file);
            
            reader.onload = (e) => {
                const img = new Image();
                img.src = e.target.result;
                
                img.onload = () => {
                    // 调整图片大小
                    const w = Math.min(this.maxWidth, img.width);
                    const h = img.height * (w / img.width);
                    
                    const canvas = document.createElement('canvas');
                    const ctx = canvas.getContext('2d');
                    
                    canvas.width = w;
                    canvas.height = h;
                    ctx.drawImage(img, 0, 0, w, h);
                    
                    // 获取压缩后的图片Base64编码
                    const base64 = canvas.toDataURL('image/jpeg', 0.8);
                    
                    // 缓存图片
                    this.images.push(base64);
                    this.imagesCount++;
                };
            };
        },
        
        // 删除图片
        removeImage(index) {
            // 从数组中移除图片
            this.images.splice(index, 1);
            this.imagesCount--;
        },
        
        // 提交表单
        submitForm() {
            // 检查图片数量
            if (this.images.length < 1) {
                this.showErrorTip('请至少选择一张图片');
                return;
            }
            
            // 检查输入内容合法性
            if (parseInt(this.formData.lostType) < 0 || parseInt(this.formData.lostType) > 1) {
                this.showErrorTip('不合法的寻找类型');
                return;
            } else if (!this.formData.name || this.formData.name.length > 25) {
                this.showErrorTip('物品名称长度不合法');
                return;
            } else if (this.formData.description && this.formData.description.length > 100) {
                this.showErrorTip('物品描述长度不合法');
                return;
            } else if (!this.formData.location || this.formData.location.length > 30) {
                this.showErrorTip('地点长度不合法');
                return;
            } else if (this.formData.itemType === '') {
                this.showErrorTip('请选择物品分类');
                return;
            } else if (parseInt(this.formData.itemType) < 0 || parseInt(this.formData.itemType) > 11) {
                this.showErrorTip('不合法的物品分类');
                return;
            } else if (!this.formData.qq && !this.formData.wechat && !this.formData.phone) {
                this.showErrorTip('联系方式至少需要填写一项');
                return;
            } else if (this.formData.qq && this.formData.qq.length > 20) {
                this.showErrorTip('不合法的QQ号码');
                return;
            } else if (this.formData.wechat && this.formData.wechat.length > 20) {
                this.showErrorTip('不合法的微信号');
                return;
            } else if (this.formData.phone && this.formData.phone.length > 11) {
                this.showErrorTip('不合法的手机号码');
                return;
            }
            
            this.loading = true;
            
            const formData = new FormData();
            
            // 添加图片
            for (let i = 0; i < this.images.length; i++) {
                const base64 = this.images[i].split(',')[1];
                const binary = atob(base64);
                const array = new Uint8Array(binary.length);
                
                for (let j = 0; j < binary.length; j++) {
                    array[j] = binary.charCodeAt(j);
                }
                
                formData.append('image' + (i + 1), new Blob([array], {type: 'image/jpeg'}));
            }
            
            // 添加表单数据
            formData.append('lostType', this.formData.lostType);
            formData.append('name', this.formData.name);
            formData.append('description', this.formData.description);
            formData.append('location', this.formData.location);
            formData.append('itemType', this.formData.itemType);
            
            if (this.formData.qq) {
                formData.append('qq', this.formData.qq);
            }
            if (this.formData.wechat) {
                formData.append('wechat', this.formData.wechat);
            }
            if (this.formData.phone) {
                formData.append('phone', this.formData.phone);
            }
            
            // 提交发布
            axios.post('/api/lostandfound/item', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
            .then(response => {
                if (response.data.success === true) {
                    // 根据物品类型跳转
                    if (parseInt(this.formData.lostType) === 0) {
                        this.$router.push('/lostandfound/lost');
                    } else {
                        this.$router.push('/lostandfound/found');
                    }
                } else {
                    this.showErrorTip(response.data.message);
                }
            })
            .catch(error => {
                console.error('发布物品信息失败:', error);
                this.showErrorTip('发布物品信息失败，请稍后重试');
            })
            .finally(() => {
                this.loading = false;
            });
        }
    }
}
</script>

<style scoped>
    /* 保持原有CSS样式，这里可以添加特定的样式 */
</style>