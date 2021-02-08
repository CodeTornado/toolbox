var Main = {data() {
        return {
            submitData: {
                word: ''
            },
            tableData: [{
                example_sentence: 'In a rousing speech, the President hit out against the trade union.',
                translate: '在一次振奋人心的讲话中，总统猛烈抨击了工会。'
            }, {
                example_sentence: 'In a rousing speech, the President hit out against the trade union.',
                translate: '在一次振奋人心的讲话中，总统猛烈抨击了工会。'
            }]
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if(valid) {
                    var _this = this;
                    var word = _this.submitData.word;
                    var paramObj = {
                        'word': word,
                    };


                    axios.get('/toolbox/wordTranslationData', {
                        headers: {},
                        params: paramObj
                    })
                        .then(function (response) {
                            if (response && response.data && response.data != "{}") {
                                _this.tableData = response.data;
                            }
                        })
                        .catch(function (error) {
                            // handle error
                            console.log(error);
                        })
                        .then(function () {
                            // always executed
                        });

                    }else{
                         console.log('error submit!!');
                         return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    }
}
var Ctor = Vue.extend(Main);
new Ctor().$mount('#app');