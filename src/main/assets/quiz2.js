//----------------------------------------------------------------- quiz2 js
var complete_btn2 = document.getElementById("quiz_view2");
var quizCode = 'kbi_1_9';

// 정답 설정 O = 1 , X = 2;
var AnswerNum = 2;
var quizSel = false;

function QuizCheckAnswer($za, $sel)
{
	
	if(quizSel) return;
	
	document['btn_'+$za].src = "http://192.168.10.200:8430/Demo/quiz2/images/" + quizCode + $sel + ".png";
	quizSel = true;
	
	if($za == AnswerNum)
	{
		QuizSuccess();
	}
	else
	{
		QuizFailed();
	}
	
}

function QuizAnswerOpen()
{
	complete_btn2.style.display = "";
	eval('quiz_answer' + AnswerNum).style.visibility = "visible";
	//viewNextBtn();
	
}

function QuizSuccess()
{
	eval('quiz_right').style.visibility = "visible";
	QuizEventSound('right');
	QuizAnswerOpen();
}

function QuizFailed()
{
	eval('quiz_wrong').style.visibility = "visible";
	QuizEventSound('wrong');
	QuizAnswerOpen();
}

	var right_audio = document.getElementById('audio1');
	right_audio.src = "http://192.168.10.200:8430/Demo/02.mp3";
	right_audio.load();
	
	var wrong_audio = document.getElementById('audio2');
	wrong_audio.src = "http://192.168.10.200:8430/Demo/03.mp3";
	wrong_audio.load();
	
function QuizEventSound($type)
{
	
	
	if($type == "right")
	{		
		right_audio.play();
		return false;
		
	}
	else if($type == "wrong")
	{

		wrong_audio.play();
		return false;
	}
}
