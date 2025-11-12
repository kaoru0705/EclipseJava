package dbapp.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OraTest {
	
	// java 언어로 오라클 서버에 접속하여, insert 문을 실행해보기!!
	public static void main(String[] args) {
		
		// 오라클 서버에 접속!! 마치 SQLPlus java/1234로 접속하는 행위를
		// java 언어로 진행해야 함...
		// javaSE에는 기본적으로 오라클, mysql, postgresql, redis, mongodb 등
		// 제어할 수 있는 클래스가 기본적으로 포함되어 있지 않음...
		// 따라서 드라이버를 이용해야 한다. 참고로 각 데이터베이스 제품마다 드라이버는
		// db를 제작한 벤더가 제공할 의무가 있다..
		
		// 오라클용 드라이버 메모리에 로드!!
		// 아래와 같이 forName 메서드를 이용하면, 동적으로 클래스가 메서드 영역(static영역)으로 올라감
		
		
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 로드 성공");
			
			// 오라클에 접속!!
			String url="jdbc:oracle:thin:@localhost:1521:XE";
			String id="java";
			String pwd="1234";
			
			// 접속이 성공되었는지 확인하려면, 접속 성공 후 반환되는 Connection
			// 인터페이스가 메모리에 올라왔는지 체크하면 된다..
			con = DriverManager.getConnection(url, id, pwd);
			
			if(con != null) {
				System.out.println("오라클 접속 성공");
				// 접속에 성공하였으므로, 레코드 1건 넣어보자!!
				String sql = "insert into student(student_id, id, pwd, name)";
				sql +=" values(seq_student.nextval, 'kim', '0000', 'HongGilDong')";
				System.out.println(sql);
				
				// 아직까지는 쿼리문을 준비만 하고, 아직 실행은 안한 상태!!
				// 자바의 데이터베이스 연동 기술을 가리켜 JDBC라 하며, 주로 java.sql 패키지에서 지원함
				// jdbc 관련 객체 중 PreparedStatement 인터페이스가 쿼리문을 수행하는 역할을 함
				pstmt = con.prepareStatement(sql); // 쿼리문을 수행할 인터페이스 메모리에 올라옴
				
				// 수행!! execute
				// executeUpdate() 메서드는 실행 후 두 가지 경우의 수 를 반환한다.
				// 성공 시: 이 쿼리문 실행에 의해 영향을 받은 레코드 수가 반환(insert에 의해 들어가는 레코드는 1건)
				// 따라서 성공 시는 1을 반환
				// 실패 시: 0을 반환
				int result = pstmt.executeUpdate(); // DML(insert, update, delete) 수행
				String msg = null;
				if(result > 0) {
					msg = "등록 성공";
				}
				else {
					msg = "등록 실패";
				}
				System.out.println(msg);
				
				
			}else {
				System.out.println("오라클 접속 실패");
			}
			
			// 작업이 끝나면 연결되어있던 데이터베이스 커넥션은 반드시!!! 끊어야 한다.(메모리 누수로 다운됨)

			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드 실패");
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			// try문이든, catch문이든 언제나 db는 닫아야 함
			// 따라서 finally에서 처리
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}				

		}
		
	}

}
