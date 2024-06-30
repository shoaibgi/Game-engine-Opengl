package core.framebuffer;

	import java.util.HashMap;

	import core.texturing.GLTexture;
	import core.target.FrameBufferObject;

	public class GLFrameBufferObject extends FrameBufferObject{

		protected GLFramebuffer frameBuffer;
		protected HashMap<Attachment, GLTexture> attachments = new HashMap<>();
		
		public GLTexture getAttachmentTexture(Attachment attachment){
			
			return attachments.get(attachment);
		}
		
		public void bind(){
			frameBuffer.bind();
		}
		
		public void unbind(){
			frameBuffer.unbind();
		}
	}


