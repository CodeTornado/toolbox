var Main = {
	data() {
		return {
			form: {
				english: ['大写', '小写'],
				number: true,
				special: true,
				no_repeat: false,
				append_pw: false,
				hope: '',
				must: '',
				avoid: '',
				password_length: 32,
				count_num: 2

			},
			default_form_data: {
				english: ['大写', '小写'],
				number: true,
				special: true,
				no_repeat: false,
				append_pw: false,
				hope: '',
				must: '',
				avoid: '',
				password_length: 32,
				count_num: 2

			},
			letter_capital_array: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
			],
			letter_lowercase_array: ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
				'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
			],
			number_array: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0'],
			special_char_array: ['~', '!', '@', '#', '$', '%', '^', '&', '*', '?', '_', '-'],
			// ↓用户输入的字符串
			hope_char_array: [],
			must_char_array: [],
			//最终生成的密码
			passwordtext: ''
		};
	},
	watch: {
		"form.hope": "hope_replace_avoid_str",
		"form.must": "must_replace_avoid_str",
		"form.avoid": ["hope_replace_avoid_str", "must_replace_avoid_str"]
	},
	methods: {
		generate_pw() {// 生成密码 这是最重要的方法!!!
			try {
				//初始化数据
				var user_form_data = this.form;

				// 参数校验  生成条件不能全禁用
				if (user_form_data.english.length == 0 && !user_form_data.number &&
					!user_form_data.special && !user_form_data.hope && !
					user_form_data.must)
					return this.$message.warning('警告哦，这点信息不能生成随机密码');

				var pass_result = user_form_data.append_pw ? this.passwordtext : '';

				this.hope_char_array = user_form_data.hope.split('');
				this.must_char_array = user_form_data.must.split('');
				
				// 开始生成密码
				//for 生成多个随机密码
				// {生成一个随机密码 并写到追加到密码框 添加换行符号
				for (var pwindex = 0; pwindex < user_form_data.count_num; pwindex++)
					pass_result += (pass_result ? "\n" : "") + this.generate_one_pw();

				//随机密码生成结束
				this.passwordtext = pass_result;
				//光标定位到密码结果框
				this.$refs.input_passwordtext_ref.focus();
				this.$message.success('成功生成密码');
			} catch (e) {
				this.$message.error('错误哦，规则错误无法生成随机密码');
				console.log(e.toString());
			}
		},
		generate_one_pw() {
			// 生成开始时间
			var startTime = new Date().getTime();
			var user_form_data = this.form;
			//(排除字符&&不重复)>必须包含>(字母大写 | 字母小写 | 数字 | 特殊符号 | 用户希望)
			var pw_arr = new Array(user_form_data.password_length);
			//必须包含的字符
			for (var i = 0; i < this.must_char_array.length; i++)
				this.char_array_random_insertion(pw_arr, this.must_char_array[i]);

			//(字母大写 | 字母小写 | 数字 | 特殊符号 | 用户希望)
			var accepted = new Array();
			if (user_form_data.english.indexOf("大写") != -1)
				accepted.push(0);

			if (user_form_data.english.indexOf("小写") != -1)
				accepted.push(1);

			if (user_form_data.number)
				accepted.push(2);

			if (user_form_data.special)
				accepted.push(3);

			if (user_form_data.hope)
				accepted.push(4);


			//  生成的字符长度是否足够   不够再空的位置继续随机生成
			while (user_form_data.password_length > pw_arr.join('').length) {
				if (new Date().getTime() - startTime > 1000) {
					throw new Error("生成循环运算时间过长,配置规则错误");
				}
				//随机一种字符 (字母大写 | 字母小写 | 数字 | 特殊符号 | 用户希望)
				var index = accepted[Math.floor(Math.random() * accepted.length)];
				switch (index) {
					case 0:
						//每种类型字符取一个 并插入到密码数组中
						// 字母 大写
						this.char_array_random_insertion(pw_arr, this.letter_capital_array);
						break;
					case 1:
						// 字母  小写
						this.char_array_random_insertion(pw_arr, this.letter_lowercase_array);
						break;
					case 2:
						// 数字
						this.char_array_random_insertion(pw_arr, this.number_array);
						break;
					case 3:
						// 特殊符号
						this.char_array_random_insertion(pw_arr, this.special_char_array);
						break;
					case 4:
						// 用户希望
						this.char_array_random_insertion(pw_arr, this.hope_char_array);
						break;

				}
			}
			return pw_arr.join('');
		},
		element_random(arr_obj) {//从数组中随机获取一个元素
			if (!arr_obj)
				throw new Error("程序问题:element_random(arr_obj)....arr_obj是空的!");;
			if (arr_obj.length == 1)
				return arr_obj[0];
			else
				return arr_obj[Math.floor(Math.random() * arr_obj.length)];

		},
		char_array_random_insertion(pw_arr, insert_char_array) {
			var insert_char = this.element_random(insert_char_array);
			if (this.no_repeatedly_checked(pw_arr, insert_char))
				return;

			var index = this.random_array_null_index(pw_arr);
			pw_arr[index] = insert_char;
		},
		no_repeatedly_checked(pw_arr, char_str) {
			return this.form.no_repeat && pw_arr.indexOf(char_str) != -1;
		},
		random_array_null_index(arr_obj) { //随机获取数组空位的索引
			if (!arr_obj)
				throw new Error("程序问题:random_array_null_index(arr_obj)....arr_obj是空的!");;
			//找出全部空位
			var null_value_index_arr = [];
			for (var i = 0; i < arr_obj.length; i++)
				if (!arr_obj[i])
					null_value_index_arr.push(i);

			//然后随机获取一个空位
			var index = Math.floor(Math.random() * null_value_index_arr.length);
			return null_value_index_arr[index];
		},
		hope_replace_avoid_str() {
			var replace_result = this.form.hope;
			var replace_result_length = this.form.hope.length;
			var avoid_str = this.form.avoid;
			if (replace_result && avoid_str) {
				//  替换掉排除字符
				for (var i = 0; i < avoid_str.length; i++)
					replace_result = replace_result.replaceAll(avoid_str[i], '');

				if (replace_result.length != this.form.hope.length) { //有变化需要修改 vue 的值
					this.$message.warning('警告哦，该字符在"排除字符"中已存在,不可同时存在');
					this.form.hope = replace_result;
				}
			}
		},
		must_replace_avoid_str() {
			var replace_result = this.form.must;
			var replace_result_length = this.form.must.length;
			var avoid_str = this.form.avoid;
			if (replace_result && avoid_str) {
				//  替换掉排除字符
				for (var i = 0; i < avoid_str.length; i++)
					replace_result = replace_result.replaceAll(avoid_str[i], '');

				if (replace_result.length != this.form.must.length) { //有变化需要修改 vue 的值
					this.$message.warning('警告哦，该字符在"排除字符"中已存在,不可同时存在');
					this.form.must = replace_result;
				}
			}
		},
		copy() {
			if (!this.$refs.input_passwordtext_ref.value) {
				this.$message.warning('警告哦，密码框还是空的');
			} else {
				this.$refs.input_passwordtext_ref.select();
				document.execCommand("Copy");
				this.$message.success('成功复制');
			}
		}
	}
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')
