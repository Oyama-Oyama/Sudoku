package viewModels


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock

class TimerViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {
    private var _time = MutableStateFlow(0L)
    val time = _time.asStateFlow()
    private var _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private var startTime = 0L
    private var timerJob: Job? = null
    private var pauseElapsedTime = 0L

    fun startOrResumeTimer() {
        if (_isRunning.value)
            return

        startTime = Clock.System.now().toEpochMilliseconds() - pauseElapsedTime
        timerJob = CoroutineScope(dispatcher).launch {
            while (isActive) {
                _time.value = Clock.System.now().toEpochMilliseconds() - startTime
                println("current timer::${_time.value}")
                delay(INTERVAL)
            }
        }

        _isRunning.value = true
    }

    fun pauseTimer() {
        pauseElapsedTime = _time.value
        timerJob?.cancel()
        _isRunning.value = false
    }

    fun resetTimer() {
        timerJob?.cancel()
        _time.value = 0L
        pauseElapsedTime = 0L
        _isRunning.value = false
    }

    companion object {
        private const val INTERVAL = 1000L
    }
}