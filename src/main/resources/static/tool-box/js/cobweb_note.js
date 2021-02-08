//获取 id
let main_id = document.getElementById("text_main_div").getAttribute("text_main_id");

var dataObj = {
    main_data: {
        text: {
            id: 1,
            title: "默认标题",
            content: "这是默认内容",
            content_html: "这是默认内容"
        },
        relationNoteDatas: [],
        labelStrList: ['标签一'],
        commentDatas: [],
    },
    selectNoteItem: null,
    inputText: '',
    select: '2',
    activeName: '1',//<<没有意义的数据, 只为展开内容
    inputNewTagVisible: false,
    inputNewTag: '',

    dialogFormVisible: false,
    noteForm: {
        title: '',
        content: ''
    },
    addNewNoteVisible: false,
    addNewNoteForm: {
        title: '',
        content: ''
    },
    commentFormVisible: false,
    commentForm: {
        content: ''
    },
    updateCommentFormVisible: false,
    updateCommentForm: {
        id: 0,
        content: ''
    },
    formLabelWidth: '50px',
    deleteNoteVisiblePrompt: false
};

var Main = {
    data() {
        return dataObj;
    },
    beforeCreate() {
        var _this = this;
        //main_id将相关信息全部查出
        var converter = new showdown.Converter({extensions: ['table']});

        var params = new URLSearchParams();
        params.append('id', main_id);
        axios.post('/cobweb/selectTextById', params)
            .then(function (response) {
                if (response && response.data) {

                    var cobwebData = response.data;
                    cobwebData.text.content_html = converter.makeHtml(cobwebData.text.content);
                    if (cobwebData.relationNoteDatas) {
                        for (var i = 0; i < cobwebData.relationNoteDatas.length; i++) {
                            if (cobwebData.relationNoteDatas[i].content) {
                                cobwebData.relationNoteDatas[i].content_html = converter.makeHtml(cobwebData.relationNoteDatas[i].content);
                            }
                        }
                    }
                    _this.main_data = response.data;
                } else {
                    _this.$message.error('无此笔记数据!!');
                }
            })
            .catch(function (error) {
                console.log(error);
            })
            .then(function () {
            });
    },
    methods: {
        submmitNewComment() {
            var _this = this;
            var params = new URLSearchParams();
            params.append('main_text_id', main_id);
            params.append('content', this.commentForm.content.trim());
            axios.post('/cobweb/addNewComment', params)
                .then(function (response) {
                    if (response && response.data > 0) {

                        //绑定到页面里
                        var commentObj = {
                            createTime: new Date().toString(),
                            content: _this.commentForm.content,
                            id: response.data,
                            textMainId: main_id
                        };
                        if (_this.main_data.commentDatas == null) {
                            _this.main_data.commentDatas = [];
                        }
                        _this.main_data.commentDatas.push(commentObj);


                        _this.commentForm.content = '';
                        _this.commentFormVisible = false;
                        _this.$message.success('评论成功');
                    } else {
                        _this.$message.error('评论失败!!');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });

        },
        submmitComment(commentId) {
            var _this = this;
            var params = new URLSearchParams();
            params.append('commentId', commentId);
            axios.post('/cobweb/delComment', params)
                .then(function (response) {
                    if (response && response.data > 0) {
                        // _this.main_data.commentDatas.push(commentObj);
                        for (var i = 0; i < _this.main_data.commentDatas.length; i++) {
                            if (_this.main_data.commentDatas[i].id == commentId) {
                                _this.main_data.commentDatas.splice(i, 1);
                                break;
                            }
                        }

                        _this.$message.success('删除评论成功');
                    } else {
                        _this.$message.error('删除评论失败!!');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        goBack() {//上方返回按钮
            console.log('go back');
        },
        addNewNoteWindow() {
            this.addNewNoteForm.title = "";
            this.addNewNoteForm.content = "";
            this.addNewNoteVisible = true;
        },
        disengage(relatedTextId) {
            var _this = this;

            var params = new URLSearchParams();
            params.append('main_text_id', main_id);
            params.append('relatedTextId', relatedTextId);
            axios.post('/cobweb/disengage', params)
                .then(function (response) {
                    if (response && response.data > 0) {
                        // _this.main_data.relationNoteDatas.reomve(_this.addNewNoteForm);
                        for (var i = 0; i < _this.main_data.relationNoteDatas.length; i++) {
                            if (_this.main_data.relationNoteDatas[i].id == relatedTextId) {
                                //删除当前位置
                                _this.main_data.relationNoteDatas.splice(i, 1);
                            }
                        }
                        _this.$message.success('删除关联成功');
                    } else {
                        _this.$message.error('删除关联失败!!');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        submmitAddNewNote() {
            var _this = this;
            var params = new URLSearchParams();
            params.append('main_text_id', main_id);
            params.append('title', this.addNewNoteForm.title);
            params.append('content', this.addNewNoteForm.content.trim());
            axios.post('/cobweb/addNewNoteAndRelevance', params)
                .then(function (response) {
                    if (response && response.data > 0) {
                        _this.addNewNoteVisible = false;
                        //绑定到页面里
                        var converter = new showdown.Converter({extensions: ['table']});
                        var content_html = converter.makeHtml(_this.addNewNoteForm.content);
                        var id = response.data;
                        if (!_this.main_data.relationNoteDatas) {
                            _this.main_data.relationNoteDatas = [];
                        }
                        var obj = {
                            id: id,
                            title: _this.addNewNoteForm.title,
                            content: _this.addNewNoteForm.content,
                            content_html: content_html
                        };
                        _this.main_data.relationNoteDatas.push(obj);

                        _this.$message.success('新增成功');
                    } else {
                        _this.$message.error('新增失败!!');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        querySearchAsync(queryString, cb) {
            if (!queryString || !queryString.trim()) {
                return;
            }

            var params = new URLSearchParams();
            params.append('id', main_id);
            params.append('type', this.select);
            params.append('content', queryString.trim());
            axios.post('/cobweb/selectByLikeContent', params)
                .then(function (response) {
                    if (response && response.data) {
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i].value = response.data[i].title;
                        }
                        cb(response.data);
                    } else {
                        _this.$message.error('查不着相关信息');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
            // var results = [
            //     {"value": "标题7", "title": "标题7", "content": "## 是水淀粉23哦呜哦 111", "id": 7},
            //     {"value": "标题8", "title": "标题8", "content": "## 8是8888水淀粉23哦呜哦 111", "id": 7}];
            // cb(results);
        },
        handleClick(commentObj) {//评论处
            this.updateCommentFormVisible = true;
            this.updateCommentForm.id = commentObj.id;
            this.updateCommentForm.content = commentObj.content;
        },
        submmitUpdateComment() {
            if (!this.updateCommentForm.content || !this.updateCommentForm.content.trim()) {
                this.$message.error('不能提交空评论!');
                return;
            }
            var _this = this;
            var params = new URLSearchParams();
            params.append('commentId', this.updateCommentForm.id);
            params.append('content', this.updateCommentForm.content);
            axios.post('/cobweb/updateComment', params)
                .then(function (response) {
                    if (response && response.data > 0) {

                        //修改页面
                        for (var i = 0; i < _this.main_data.commentDatas.length; i++) {
                            if (_this.main_data.commentDatas[i].id == _this.updateCommentForm.id) {
                                _this.main_data.commentDatas[i].content = _this.updateCommentForm.content;
                                break;
                            }
                        }

                        _this.updateCommentForm.content = '';
                        _this.updateCommentFormVisible = false;
                        _this.$message.success('评论修改成功');
                    } else {
                        _this.$message.error('评论修改失败!!');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        handleSelect(item) {
            this.selectNoteItem = item;
        },
        deleteMainNote() {
            var _this = this;
            var params = new URLSearchParams();
            params.append('id', main_id);
            axios.post('/cobweb/deleteNoteById', params)
                .then(function (response) {
                    if (response && response.data > 0) {
                        _this.deleteNoteVisiblePrompt = false;
                        _this.$message.success("删除成功");
                    } else {
                        _this.$message.error("删除失败!!");
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        showUpdateNoteWindow() {
            this.noteForm.title = this.main_data.text.title;
            this.noteForm.content = this.main_data.text.content;
            this.dialogFormVisible = true
        },
        submmitUpdateNote() {
            var _this = this;

            var params = new URLSearchParams();
            params.append('id', main_id);
            params.append('title', this.noteForm.title.trim());
            params.append('content', this.noteForm.content.trim());
            axios.post('/cobweb/updateNoteById', params)
                .then(function (response) {
                    if (response && response.data && response.data > 0) {
                        _this.main_data.text.content = _this.noteForm.content.trim();

                        var converter = new showdown.Converter({extensions: ['table']});
                        _this.main_data.text.content_html = converter.makeHtml(_this.noteForm.content.trim());
                        _this.dialogFormVisible = false;
                        _this.$message.success("修改成功");
                    } else {
                        _this.$message.error("修改失败");
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        deleteLabelAssociation(tag) {

            var _this = this;

            var params = new URLSearchParams();
            params.append('textMainId', main_id);
            params.append('labelName', tag);
            axios.post('/cobweb/deleteLabelAssociation', params)
                .then(function (response) {
                    if (response && response.data > 0) {
                        _this.$message.success("删除标签成功");
                        _this.main_data.labelStrList.splice(_this.main_data.labelStrList.indexOf(tag), 1);
                    } else {
                        _this.$message.error("删除标签失败!!");
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });

        },
        addNewLink() {
            if (this.selectNoteItem != null && this.inputText && this.selectNoteItem.title == this.inputText) {
                //如果没有关系 可以添加
                var addTextId = this.selectNoteItem.id;
                if (addTextId == main_id) {
                    this.$message.error("不能自关联");
                    return;
                }
                var isRepeatAssociated = false;
                if (this.main_data.relationNoteDatas) {
                    for (var i = 0; i < this.main_data.relationNoteDatas.length; i++) {
                        if (this.main_data.relationNoteDatas[i].id == addTextId) {
                            isRepeatAssociated = true;
                            break;
                        }
                    }
                }
                if (isRepeatAssociated) {
                    this.$message.error("不能重复关联");
                    return;
                }
                var _this = this;

                var params = new URLSearchParams();
                params.append('main_id', main_id);
                params.append('addTextId', addTextId);
                axios.post('/cobweb/associatedNote', params)
                    .then(function (response) {
                        if (response && response.data && response.data == 2) {
                            var converter = new showdown.Converter({extensions: ['table']});
                            _this.selectNoteItem.content_html = converter.makeHtml(_this.selectNoteItem.content);
                            if (!_this.main_data.relationNoteDatas) {
                                _this.main_data.relationNoteDatas = [];
                            }
                            _this.main_data.relationNoteDatas.push(_this.selectNoteItem);
                        } else {
                            _this.$message.error("关联失败!");
                        }
                    })
                    .catch(function (error) {
                        console.log(error);
                    })
                    .then(function () {
                    });

            } else {
                this.$message.error("必须选中已有笔记");
            }

        },
        showInput() {
            this.inputNewTagVisible = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        inputNewTagConfirm() {
            let inputNewTag = this.inputNewTag;
            if (!inputNewTag || !inputNewTag.trim()) {
                this.inputNewTagVisible = false;
                this.inputNewTag = '';
                return;
            }
            inputNewTag = inputNewTag.trim();

            if (this.main_data.labelStrList != null) {
                for (var i = 0; i < this.main_data.labelStrList.length; i++) {
                    if (this.main_data.labelStrList[i] == inputNewTag) {
                        this.$message.error("不能重复添加标签!!");
                        return;
                    }
                }
            }
            var _this = this;

            var params = new URLSearchParams();
            params.append('textMainId', main_id);
            params.append('tagName', inputNewTag);
            axios.post('/cobweb/addNewTagAndRelevance', params)
                .then(function (response) {
                    if (response && response.data > 0) {
                        _this.$message.success("添加标签成功");
                        _this.inputNewTagVisible = false;
                        _this.inputNewTag = '';
                        if (_this.main_data.labelStrList == null) {
                            _this.main_data.labelStrList = [];
                        }
                        _this.main_data.labelStrList.push(inputNewTag);
                    } else {
                        _this.$message.error("添加标签失败!!");
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });

        }, addComment() {
            console.log('addComment');
        }
    }
}
var Ctor = Vue.extend(Main);
new Ctor().$mount('#app');
