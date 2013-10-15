package org.genshin.scrollninjaeditor.factory;

/**
 * オブジェクトを生成するインタフェース。
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <K>	オブジェクトを判別するための識別子
 * @param <V>	生成するオブジェクトの型
 */
public interface FactoryInterface<K, V>
{
	/**
	 * オブジェクトを取得する。
	 * @param key	オブジェクトの識別子
	 * @return		オブジェクト
	 */
	public V get(K key);
}
