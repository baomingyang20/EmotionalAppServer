package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import frame.ServerMainFrame;
import handleDate.DataProcess;
import mailServer.DB.DBUse;


public class ServerMain {

	private static final int BUF_SIZE = 1024;
	private static final int PORT = 9999;
	private static final int TIMEOUT = 3000;
	private static DBUse dbuse = DBUse.getDBUse();
	private DataProcess dataProcess = DataProcess.getDataProcess();
	private static Map<String, SelectionKey> onLineMap = new HashMap<>();
	private ServerMainFrame frame = ServerMainFrame.getFrame();

	public static void main(String[] args) {
		ServerMain sm = new ServerMain();
		sm.selector();
	}

	public static void setOnLineMap(String useName, SelectionKey key) {
		onLineMap.put(useName, key);
	}

	public static SelectionKey getOnLineMap(String useName) {
		return onLineMap.get(useName);
	}

	public static void removeOnLineMap(String useName) {
		onLineMap.remove(useName);
	}

//	SelectionKey key1 = null;
//	SelectionKey key2 = null;

	/*
	 * handleAccept中的SelectionKey和handleRead，handleWrite的SelectionKey是不一样的
	 */
	public void handleAccept(SelectionKey key) throws IOException {
		ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
		SocketChannel sc = ssChannel.accept();
		System.out.println("连接成功");
		sc.configureBlocking(false);
		sc.register(key.selector(), SelectionKey.OP_READ);
	}

	public void handleRead(SelectionKey key) {

		if (null == key.attachment()) {
			AttachMessage am = new AttachMessage();
			key.attach(am);
		}

		SocketChannel sc = (SocketChannel) key.channel();
		AttachMessage am = (AttachMessage) key.attachment();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String useName = am.getUseName();
		try {
			ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
			int count = 0;
			count = sc.read(buffer);
			String request = "";

			if (count > 0) {
				buffer.flip();
				request = new String(buffer.array());
			} else {

				if (am.getState() == AttachMessage.LOGGED_IN) {
					dbuse.logOut(useName);
				}

				String date = simple.format(new Date());
				frame.addRow(new String[] { useName, "end", date });
				removeOnLineMap(am.getUseName());

				sc.close();
				return;
			}

			key.interestOps(SelectionKey.OP_WRITE);
			request = request.trim();

			am.setMessage(request);

			dataProcess.handleString(am, key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (am.getState() == AttachMessage.LOGGED_IN) {
				dbuse.logOut(am.getUseName());
			}
			String date = simple.format(new Date());
			frame.addRow(new String[] { useName, "end", date });
			removeOnLineMap(am.getUseName());
			key.cancel();
			try {
				sc.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void handleWrite(SelectionKey key) {

//		key2 = key;
//		System.out.println("key1:" + key1);
//		System.out.println("key2:" + key2);
//		if (key1 == key2) {
//			System.out.println(true);
//		} else {
//			System.out.println(false);
//		}

		SocketChannel s = (SocketChannel) key.channel();
		AttachMessage am = (AttachMessage) key.attachment();
		try {

			s.write(ByteBuffer.wrap(am.getMessage().getBytes()));

			am.setMessage("");
			key.attach(am);
			key.interestOps(SelectionKey.OP_READ);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			key.cancel();
			try {
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void selector() {
		Selector selector = null;
		ServerSocketChannel ssc = null;

		try {
			selector = Selector.open();
			ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(PORT));
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				if (selector.select(TIMEOUT) == 0) {
					// System.out.println("------");
					continue;
				}

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();

					if (key.isAcceptable()) {
						System.out.println("key accept");
						handleAccept(key);
					}

					if (key.isReadable()) {
//						System.out.println("key read");
						handleRead(key);
					}

					if (key.isValid() && key.isWritable()) {
//						System.out.println("key write");
						handleWrite(key);
					}
					it.remove();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

}
