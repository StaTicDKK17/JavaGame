package Main;

import java.awt.Canvas;	
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{ // this class have access to methods from the class Canvas 
	
	private static final long serialVersionUID = 1L; // no idea what this shit does tho!
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9; // creates width and height for the game frame
	private boolean running = false; // tells the program if its on or off
	private Thread thread; // creates a thread in memory
	
	
	private Handler handler; // creates a instance of the class handler for later use
	
	public Game(){ // the main method
		handler = new Handler(); // instanciate the handler class
		this.addKeyListener(new KeyInput(handler)); // listen for user input
		
		new Window(WIDTH, HEIGHT, "Let's Build a Game!", this);  // initialize a new window
		
		handler.addObject(new Player(WIDTH/2-32, HEIGHT/2-32, ID.Player)); // creates a new player for the game
		handler.addObject(new BasicEnemy(WIDTH/2-32, HEIGHT/2-32, ID.BasicEnemy)); // creates a new player for the game
	}
	public synchronized void start(){ // the start method for the game
		thread = new Thread(this); // initializing the thread
		thread.start(); // making the thread start
		running = true; // makes the program know its on
	}
	
	public synchronized void stop(){ // method for stopping the game
		try { // simple try/catch statement
			thread.join(); // killing the thread we started in the start method
			running = false; // turns off running variable
		} catch(Exception e){ // if an error occurs in the try...
			e.printStackTrace(); // print if there is any errors
		}
	}
	
	public void run() { // the complex popular used game loop for java
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick(); // added own method
				delta--;
			}
			if(running)
				render(); // added own method
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop(); // self-created stop method
	}
	
	private void tick() {
		handler.tick();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
		
		
	}
	public static void main(String args[]){
		new Game(); // see Game method
	}
	
}
