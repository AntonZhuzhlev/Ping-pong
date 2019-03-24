package application;
	
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	
	private static final int width_table = 1024;
	private static final int height_table = 512;
	private static final int width_frame = 20;
	private static final int height_frame = 20;
	private static final int width_racket = 15;
	private static final int height_racket = 125;
	private static final int diameter_ball = 25;
	private static final double quotient = 1;
	private static final double radius_ball = diameter_ball/2;
	private static final int k_x = 6;
	private static final int c_x = 5;
	private static final int k_y = 5;
	private static final int c_y = 3;
	
	double ball_speed_x = k_x*Math.random() + c_x;
	double ball_speed_y = k_y*Math.random() + c_y;
	double start_player_x = width_frame;
	double start_player_y = height_table/2 + height_frame/2 - height_racket/2;
	double player_x = start_player_x;
	double player_y = start_player_y;
	double start_comp_x = width_table + width_frame - width_racket;
	double start_comp_y = height_table/2 + height_frame/2 - height_racket/2;
	double comp_x = start_comp_x;
	double comp_y = start_comp_y;
	double start_ball_x = width_table/2 + width_frame/2 - radius_ball;
	double start_ball_y = height_table/2 + height_frame/2 - radius_ball;
	double ball_x = start_ball_x;
	double ball_y = start_ball_y;
	
	GraphicsContext gc;
	
	boolean gameContinues;

	int start_direction_x = 1;
	int start_direction_y = 1;
	int direction_x = start_direction_x;
	int direction_y = start_direction_y;
	// Direction nuzna chtobi zadat' napravlenie poleta 
	
	private void drawTable() {
		gc.setFill(Color.YELLOW);
		gc.fillRect(0, 0, width_table + 2 * width_frame, height_table + 2 * height_frame);
		gc.setFill(Color.WHITE);
		gc.fillRect(width_frame, height_frame, width_table, height_table);
		
		gc.setFill(Color.BLACK);
		
		if (gameContinues) {
			double ball_center_x = ball_x + radius_ball;
			double ball_center_y = ball_y + radius_ball;
			double ball_left = ball_x;
			double ball_right = ball_x + diameter_ball;
			double ball_up = ball_y;
			double ball_down = ball_y + diameter_ball;
			double player_up = player_y;
			double player_down = player_y + height_racket;
			// Chtobi udobnee bilo potom
			double ball_vector_speed_x = direction_x * ball_speed_x;
			double ball_vector_speed_y = direction_y * ball_speed_y;
			// Skorost dolzna imet napravlenie
			if (ball_down + ball_vector_speed_y > height_table + height_frame) {
				direction_y *= -1;
			}
			if (ball_up + ball_vector_speed_y < height_frame) {
				direction_y *= -1;
			}
			if (ball_right + ball_vector_speed_x > width_table - width_racket + width_frame) {
				direction_x *= -1;
			}
			if (ball_left + ball_vector_speed_x < width_racket + width_frame) {
				if ((player_down - ball_center_y >= 0) && (player_up - ball_center_y <= 0)) {
					direction_x *= -1;
				} else {
					gameContinues = false;
				}
			}
			ball_x = ball_x + ball_vector_speed_x;
			ball_y = ball_y + ball_vector_speed_y;
			if (width_table + width_frame - width_racket - ball_right < quotient*width_table) {
				comp_y = ball_center_y - height_racket/2;
			}
			gc.fillOval(ball_x, ball_y, diameter_ball, diameter_ball);
		} else {
			gc.setStroke(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.strokeText("Click to start", width_table/2, height_table/2);
			ball_x = start_ball_x;
			ball_y = start_ball_y;
			comp_x = start_comp_x;
			comp_y = start_comp_y;
			player_x = start_player_x;
			player_y = start_player_y;
			int[] x = new int[4];
			int[] y = new int[4];
			x[0] = 1;
			x[1] = 1;
			x[2] = 1;
			x[3] = 1;
			y[0] = 1;
			y[1] = -1;
			y[2] = 1;
			y[3] = -1;
			int n = (int)(4*Math.random());
			if (n == 4) {
				n = 0;
			}
			direction_x = start_direction_x*x[n];
			direction_y = start_direction_y*y[n];
			ball_speed_x = k_x*Math.random() + c_x;
			ball_speed_y = k_y*Math.random() + c_y;
		}

		gc.fillRect(player_x, player_y, width_racket, height_racket);
		gc.fillRect(comp_x, comp_y, width_racket, height_racket);
	}

	@Override
	public void start(Stage root) {
		Canvas canvas = new Canvas(width_table + 2 * width_frame, height_table + 2 * height_frame);
		root.setScene(new Scene(new StackPane(canvas)));
		root.setTitle("Ping-pong");
		gc = canvas.getGraphicsContext2D();
		
		drawTable();
		Timeline t1 = new Timeline(new KeyFrame(Duration.millis(10),e -> drawTable()));
		t1.setCycleCount(Timeline.INDEFINITE); 
		
		canvas.setOnMouseClicked(e -> gameContinues = true);
		canvas.setOnMouseMoved(e -> player_y = e.getY() - height_racket/2);
		
		root.show();
		t1.play();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
