//--------------------------   quiz1  js
            var quizCode = 'kbi_1_5';
var dragNum = 4;
var checkString = 'Btn';
var complete_btn = document.getElementById("quiz_view1");

window.addEventListener( "load", _load , false);

// 기본 위치값 저장, 해당 영역 벗어났을지 기본 위치로 지정
var defBtnsPos = [];

// 해당 영역의 좌표 값 설정
				//	   0		1		  2	    	3			4
				// left,right
var targetXpos = [[225,null],[225,null],[null,513],[225,null],[225,513]];
var targetYpos = [[271,null],[313,null],[null,271],[355,null],[355,313]];

// 좌측 해당 번호 정렬
var leftArray = [ 0, 1, 3 ];

// 우측 해당 번호 정렬
var rightArray = [ 2, 4 ];

// 좌측 영역 설정, 좌측 최소 위치값 , 좌측 최대 위치값 지정
				//	x			y
var leftArea = [[177,414], [240,371]];

// 우측 영역 설정, 우측 최소 위치값 , 우측 최대 위치값 지정
				//	x			y
var rightArea = [[467,670], [240,330]];

// 최종 위치 이동값 저장
var traceXpos;
var traceYpos;

// 좌 우측 영역 체크된 수량 저장
var leftPosCounter = 0;
var rightPosCounter = 0;

//	위치가 저장된 아이템 저장
var setItemArray = new Array();

function _load()
{
	for(var i = 0; i <= dragNum; i++)
	{
		var Btns = document.getElementById('Btn' + i);
		Btns.addEventListener('touchstarts', _touchstart, false);
		Btns.addEventListener('touchmove', _touchmove, false);
		Btns.addEventListener('touchend', _touchend, false);	
		
		var defXpos = Btns.style.left;
		var defYpos = Btns.style.top;
		
		// 초기 위치 저장
		defBtnsPos.push([defXpos,defYpos]);		
	}
}


function _touchstart(event)
{	
	event.preventDefault();
	var targetName = event.target.name;
	var b = document.getElementById(targetName);
	var n = event.target.name.substring(checkString.length , targetName.length); 
	
	if(getFlagItem(n)) return;
	
	document[targetName].src = 'http://192.168.10.200:8430/Demo/quiz1/images/' + quizCode + '_qzBtn' + n + '_over.png'
}

function _touchmove(event)
{	
	event.preventDefault();
	var targetName = event.target.name;
	var b = document.getElementById(targetName);
	var n = event.target.name.substring(checkString.length , targetName.length); 
	
	if(getFlagItem(n)) return;
		
	var xbox = b.offsetWidth/2;
	var ybox = b.offsetHeight/2;
	
	var Bx = event.targetTouches[0].pageX;
	var By = event.targetTouches[0].pageY;
	
	var tgX = Bx - xbox;
	var tgY = By - ybox;
	
	b.style.left = tgX + 'px';
	b.style.top = tgY + 'px';	
	
	traceXpos = tgX;
	traceYpos = tgY;
}


function _touchend(event)
{
	var targetName = event.target.name;
	var b = document.getElementById(targetName);
	var n = event.target.name.substring(checkString.length , targetName.length); 
	
	var xbox = b.offsetWidth/2;
	var ybox = b.offsetHeight/2;
	
	var tgX;
	var tgY;
	
	if(getFlagItem(n)) return;
	
	for(var i = 0; i < leftArray.length; i++)
	{
		if(leftArray[i] == n)
		{
			// 좌측 영역 위치 확인 및 정렬
			if(traceXpos >= leftArea[0][0] && traceXpos <= leftArea[0][1] && traceYpos >= leftArea[1][0] && traceYpos <= leftArea[1][1])
			{
				
				tgX = targetXpos[leftArray[leftPosCounter]][0];
				tgY = targetYpos[leftArray[leftPosCounter]][0];
				b.style.left = tgX + 'px';
				b.style.top = tgY + 'px';
				leftPosCounter++;
				setItemArray.push(n);
				QuizEventSound('right');
				checkGameEnd();
				return;
			}
			else
			{
				tgX = defBtnsPos[[n]][0];
				tgY = defBtnsPos[[n]][1];	
				b.style.left = tgX;
				b.style.top = tgY;
				document[targetName].src = 'http://192.168.10.200:8430/Demo/quiz1/images/' + quizCode + '_qzBtn' + n + '_on.png';
				QuizEventSound('wrong');
				return;
			}
		}
		else
		{
			// 우측 영역 위치 확인 및 정렬
			for(var j = 0; j < rightArray.length; j++)
			{
				if(rightArray[j] == n)
				{
					if(traceXpos >= rightArea[0][0] && traceXpos <= rightArea[0][1] && traceYpos >= rightArea[1][0] && traceYpos <= rightArea[1][1])
					{
						tgX = targetXpos[rightArray[rightPosCounter]][1];
						tgY = targetYpos[rightArray[rightPosCounter]][1];
						b.style.left = tgX + 'px';
						b.style.top = tgY + 'px';
						rightPosCounter++;
						setItemArray.push(n);
						QuizEventSound('right');
						checkGameEnd();
						return;
					}
					else
					{
						tgX = defBtnsPos[[n]][0];
						tgY = defBtnsPos[[n]][1];	
						b.style.left = tgX;
						b.style.top = tgY;
						document[targetName].src = 'http://192.168.10.200:8430/Demo/quiz1/images/' + quizCode + '_qzBtn' + n + '_on.png';
						QuizEventSound('wrong');
						return;
					}
				}
				
			}
		
		}
	}	
}


// 게임 완료 위치 > 네비 nextImg 표출 
var _checkGameEndCounter = 0;
function checkGameEnd()
{
	_checkGameEndCounter++;	
	
 
	if(_checkGameEndCounter > dragNum)
	{
  
		complete_btn.style.display = "";
	}
}

//function viewNextBtn()
//{	
//	location.replace("allflash://?showEndImg=NEXT");	
//}



function getFlagItem($n)
{
	for(var i = 0; i < setItemArray.length; i++)
	{
		if(setItemArray[i] == $n)	
		{
			return true;
		}
	}
}

// 퀴즈 정답 확인시 Sound 효과 설정



function trace($msg)
{
	var traceDiv = document.getElementById('traceDiv');
	var output = "<div id='traceDiv' style='position:absolute; width:300px; height:300px; left:0; top:0; 	z-index:1000;'>" + $msg + "</div>"
	traceDiv.innerHTML = output;
}