package com.mantono.felice.api.worker

import com.mantono.felice.api.Interceptor
import com.mantono.felice.api.MessageConsumer
import com.mantono.felice.api.RetryPolicy
import com.mantono.felice.api.WorkerBuilder
import org.apache.kafka.common.serialization.Deserializer
import java.time.Duration

class Worker<K, V>(
	val topics: Set<String>,
	val config: Map<String, Any>,
	val pipeline: List<Interceptor>,
	val deserializeKey: Deserializer<K>,
	val deserializeValue: Deserializer<V>,
	val retryPolicy: RetryPolicy,
	val timeout: Duration,
	consumer: MessageConsumer<K, V>
): MessageConsumer<K, V> by consumer {

	init {
		verifyState()
	}

	private fun verifyState() {
		require(topics.isNotEmpty()) { "At least one topic must be given" }
		KafkaConfig.Consumer.required.forEach {
			require(it in config.keys) {
				"Missing value for required consumer config setting: $it"
			}
		}
	}

	companion object {
		fun <K, V> builder(): WorkerBuilder<K, V> = WorkerBuilder()
	}
}